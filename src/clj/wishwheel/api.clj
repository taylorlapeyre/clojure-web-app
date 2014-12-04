(ns wishwheel.api
  "Functions relating to getting, saving, updating, and deleting data."
  (:require [ring.util.response :as ring]
            [oj.core :as oj]
            [cheshire.core :as json]))

;;---------------------------
;; Database stuff

(def db {:subprotocol "mysql"
         :subname "//127.0.0.1:3306/wishwheel3"
         :user "root"
         :password ""})

(defn find-all
  "Return all records from a particular table. Optionally takes a key/value
  to filter the results by before returning."
  ([table]
    (oj/exec {:table table} db))
  ([table col val]
    (oj/exec {:table table
              :where {col val}} db)))

(defn find-one
  "Return the first record from table that matches the given key/value, or nil."
  [table col val]
  (first (find-all table col val)))

(defn create
  "Creates a new record in table with the given data. Does not validate, will
  throw on integrity violation."
  [table data]
  (let [key (:generated_key (first (oj/exec {:table table :insert data} db)))]
    (find-one table :id key)))

(defn add-user-to-group
  [user-id group-id]
  (oj/exec {:table :users_groups
            :insert {:group_id group-id
                     :user_id user-id}} db))

(defn find-all-users-in-group
  [group-id]
  (let [memberships (find-all :users_groups :group_id group-id)]
    (if (empty? memberships)
      []
      (oj/exec {:table :users
                :select [:email :first_name :last_name]
                :where {:id (map :user_id memberships)}} db))))

;;---------------------------
;; HTTP helpers

(defn json-response
  "Takes a Clojure data structure and returns a Ring HTTP response containing
  the data in json form."
  [data]
  (-> (json/encode data)
      (ring/response)
      (ring/content-type "application/json")))

(defn unauthorized
  "Returns a 403 Ring HTTP response."
  []
  (-> (ring/response "Invalid Credentials.")
      (ring/status 403)
      (ring/content-type "application/json")))

(defn bad-request
  "Returns a 400 Ring HTTP response."
  [message]
  (-> (ring/response message)
      (ring/status 400)
      (ring/content-type "application/json")))

;; ---------------------------
;; Item handlers

(defn items-index
  "Returns a Ring response containing every item in the db in JSON form."
  [request]
  (json-response (find-all :items)))

(defn items-show
  "Returns a Ring response containing a particular item specified by
  an id. If it exists, the data is returned in JSON form. Else, 404."
  [request]
  (if-let [item (find-one :items :id (get-in request [:params :id]))]
    (json-response item)
    (ring/not-found "No item found with that id.")))

;; ---------------------------
;; Group handlers

(defn valid-group-data?
  [{:keys [name]}]
  (and (string? name)
       (not (empty? name))))

(defn groups-index
  "Returns a Ring response containing every group in the db in JSON form."
  [request]
  (json-response (find-all :groups)))

(defn groups-show
  "Returns a Ring response containing a particular group specified by
  an id. If it exists, the data is returned in JSON form. Else, 404."
  [request]
  (if-let [group (find-one :groups :id (get-in request [:params :id]))]
    (json-response (-> group
                       (assoc :wheels (find-all :wheels :group_id (:id group)))
                       (assoc :users (find-all-users-in-group (:id group)))))
    (ring/not-found "No group found with that id.")))

(defn groups-create
  "Creates a new group. If the data provided is valid, returns a 201 with the
  location of the created group. Else, 400."
  [{:keys [body] :as request}]
  (let [{:keys [group token]} (json/decode (slurp body) true)
        api-user (find-one :users :token token)]
    (cond (not api-user)                  (unauthorized)
          (not group)                     (ring/not-found "The requested group does not exist.")
          (not (valid-group-data? group)) (bad-request "Invalid group data.")
          :else
          (let [created-group (create :groups (assoc group :user_id (:id api-user)))]
            (add-user-to-group (:id api-user) (:id created-group))
            (ring/created (str "/api/groups/" (:id created-group))
                          (json/encode created-group))))))

(defn groups-adduser
  "Adds a user to the group. If a valid email is given, returns a blank 200.
  If the api user is not in the group, 403. If the user does not exist, 404."
  [{:keys [params body] :as request}]
  (let [{:keys [email token]} (json/decode (slurp body) true)
        {:keys [group-id]} params
        group (find-one :groups :id group-id)
        api-user (find-one :users :token token)
        potential-user (find-one :users :email email)]
    (cond (not group)          (ring/not-found "The requested group does not exist.")
          (not api-user)       (unauthorized)
          (not potential-user) (ring/not-found "The requested user does not exist.")
          :else
          (let [emails-in-group (into #{} (map :email (find-all-users-in-group (:id group))))]
            (if (or (contains? emails-in-group email)
                    (not (contains? emails-in-group (:email api-user))))
              (unauthorized)
              (do (add-user-to-group (:id potential-user) group-id)
                  (json-response potential-user)))))))

;; ---------------------------
;; Wheel handlers

(defn wheels-show
  "Returns a Ring response containing a particular wheel specified by
  an id. If it exists, the data is returned in JSON form. Else, 404."
  [request]
  (if-let [wheel (find-one :wheels :id (get-in request [:params :id]))]
    (json-response (-> wheel
                       (assoc :items (find-all :items :wheel_id (:id wheel)))
                       (assoc :user  (find-one :users :id (:user_id wheel)))))
    (ring/not-found "No wheel found with that id.")))

;; ---------------------------
;; User handlers

(defn valid-user-data?
  [{:keys [email password first_name last_name]}]
  (and email password first_name last_name))

(defn authenticate-user
  "Given a email and password, determines if the pair are valid credentials.
  If so, returns the user. Else, 403."
  [{:keys [body] :as request}]
  (let [{:keys [email password]} (json/decode (slurp body) true)
        user (find-one :users :email email)]
    (if (and user (= password (:password user)))
      (json-response (dissoc user :password))
      (unauthorized))))

(defn users-create
  "Creates a new user. If the data provided is valid, returns a 201 with the
  location of the created user. Else, 400."
  [{:keys [body] :as request}]
  (let [{:keys [user]} (json/decode (slurp body) true)]
    (if (and user (valid-user-data? user))
      (let [created-user (create :users user)]
        (ring/created (str "/api/users/" (:id created-user))
                      (json/encode created-user)))
      (bad-request "Invalid User data."))))