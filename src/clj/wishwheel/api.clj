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


;;---------------------------
;; Fake database stuff

(def items-table  [{:user_id nil, :wheel_id 1, :image "/public/images/system/sdh324kj.png", :price 15.00M, :name "Rubix Cube", :id 1} {:user_id nil, :wheel_id 1, :image "/public/images/system/sdfl346.png", :price 600.00M, :name "Volcano", :id 2} {:user_id nil, :wheel_id 1, :image "/public/images/system/lmhjf46.png", :price 86.00M, :name "Ear Rings", :id 3} {:user_id nil, :wheel_id 2, :image "/public/images/system/rthfhg4.png", :price 23.00M, :name "Stir Crazy", :id 4} {:user_id nil, :wheel_id 2, :image "/public/images/system/iuh434.png", :price 299.00M, :name "iPhone 6", :id 5} {:user_id nil, :wheel_id 3, :image "/public/images/system/ps4.png", :price 299.00M, :name "Playstation 4", :id 6} {:user_id nil, :wheel_id 3, :image "/public/images/system/purp_nugs.png", :price 300.00M, :name "Purple Erkle", :id 7} {:user_id nil, :wheel_id 3, :image "/public/images/system/rolex.png", :price 5000.00M, :name "Rolex", :id 8} {:user_id nil, :wheel_id 3, :image "/public/images/system/xbox1.png", :price 349.00M, :name "Xbox One", :id 9} {:user_id nil, :wheel_id 3, :image "/public/images/system/ipad3.png", :price 299.00M, :name "iPad", :id 10} {:user_id nil, :wheel_id 3, :image "/public/images/system/htcMe.png", :price 599.00M, :name "HTC One M8", :id 11} {:user_id nil, :wheel_id 5, :image "/public/images/system/sdfsdfpph3244kj.png", :price 780.00M, :name "David Yurman Bracelet", :id 12} {:user_id nil, :wheel_id 5, :image "/public/images/system/ssdfdfl348396.png", :price 40.00M, :name "Pandora Charm", :id 13} {:user_id nil, :wheel_id 5, :image "/public/images/system/lmhsdfjf46r.png", :price 87.00M, :name "Fossil Watch", :id 14} {:user_id nil, :wheel_id 6, :image "/public/images/system/ksdk34.png", :price 67.00M, :name "Hammock", :id 15} {:user_id nil, :wheel_id 6, :image "/public/images/system/kjfiw984.png", :price 35.00M, :name "Chromecast", :id 16} {:user_id nil, :wheel_id 6, :image "/public/images/system/4iw4.png", :price 12.00M, :name "White Beanie", :id 17} {:user_id nil, :wheel_id 7, :image "/public/images/system/sdh324kj.png", :price 16.00M, :name "Rubix Cube", :id 18} {:user_id nil, :wheel_id 7, :image "/public/images/system/sdfl346.png", :price 600.00M, :name "Volcano", :id 19} {:user_id nil, :wheel_id 7, :image "/public/images/system/lmhjf46.png", :price 86.00M, :name "Ear Rings", :id 20} {:user_id nil, :wheel_id 8, :image "/public/images/system/rthfhg4.png", :price 23.00M, :name "Stir Crazy", :id 21} {:user_id nil, :wheel_id 8, :image "/public/images/system/iuh434.png", :price 299.00M, :name "iPhone 6", :id 22} {:user_id nil, :wheel_id 9, :image "/public/images/system/zxc45j.png", :price 8.00M, :name "Notebook", :id 23} {:user_id nil, :wheel_id 9, :image "/public/images/system/s96.png", :price 100.00M, :name "Mechanical Keyboard", :id 24} {:user_id nil, :wheel_id 10, :image "/public/images/system/uiodk67.png", :price 12.00M, :name "Wine Glass", :id 25} {:user_id nil, :wheel_id 10, :image "/public/images/system/isw854.png", :price 6.00M, :name "Sharpie", :id 26} {:user_id nil, :wheel_id 11, :image "/public/images/system/zxc4yyy5j.png", :price 4.00M, :name "Butterfinger Bites", :id 27} {:user_id nil, :wheel_id 11, :image "/public/images/system/s96yy8.png", :price 999.00M, :name "Creative Cloud", :id 28} {:user_id nil, :wheel_id 12, :image "/public/images/system/uioyyydk7.png", :price 52.00M, :name "Kindle", :id 29} {:user_id nil, :wheel_id 12, :image "/public/images/system/isw8554q4.png", :price 3.00M, :name "Andriod Charger", :id 30} {:user_id nil, :wheel_id 11, :image "/public/images/system/zrt8j.png", :price 500.00M, :name "50in TV", :id 31} {:user_id nil, :wheel_id 11, :image "/public/images/system/kdu2.png", :price 450.00M, :name "Xbox One", :id 32} {:user_id nil, :wheel_id 12, :image "/public/images/system/upsdj2.png", :price 93.00M, :name "Golf club", :id 33} {:user_id nil, :wheel_id 12, :image "/public/images/system/kdns11.png", :price 88.00M, :name "Raybans", :id 34}])
(def groups-table [{:user_id 1, :name "Christmas 2014", :id 1} {:user_id 3, :name "Bergeron Christmas", :id 2} {:user_id 5, :name "Girls' Christmas 2014", :id 3} {:user_id 7, :name "super christmas", :id 4} {:user_id 9, :name "Roommate Christmas 2k14", :id 5} {:user_id 11, :name "12/25/14", :id 6} {:user_id 13, :name "Brother bonding", :id 7}])
(def wheels-table [{:user_id 1, :group_id 1, :name "Taylor's Christmas List", :id 1} {:user_id 2, :group_id 1, :name "Jeff's Christmas List", :id 2} {:user_id 3, :group_id 2, :name "Andrew’s Christmas List", :id 3} {:user_id 4, :group_id 2, :name "Bergeron’s Christmas List", :id 4} {:user_id 5, :group_id 3, :name "Kelsey's Christmas List", :id 5} {:user_id 6, :group_id 3, :name "Laura's Christmas List", :id 6} {:user_id 7, :group_id 4, :name "Taylor's Christmas List", :id 7} {:user_id 8, :group_id 4, :name "Jeff's Christmas List", :id 8} {:user_id 9, :group_id 4, :name "Erin's Christmas List", :id 9} {:user_id 10, :group_id 4, :name "Carol's Christmas List", :id 10} {:user_id 11, :group_id 5, :name "Kelli's Christmas List", :id 11} {:user_id 12, :group_id 5, :name "Savannah's Christmas List", :id 12} {:user_id 13, :group_id 6, :name "Alex's Christmas List", :id 13} {:user_id 14, :group_id 6, :name "Adam's Christmas List", :id 14}])
(def users-table  [{:token "09db12c6-78fc-11e4-b4ce-a1001a8342c4", :last_name "Lapeyre", :first_name "Taylor", :password "password123", :email "taylor@email.com", :id 1} {:token "09db173a-78fc-11e4-b4ce-a1001a8342c4", :last_name "McPerson", :first_name "Jeff", :password "password123", :email "jeff@email.com", :id 2} {:token "09dbf3d0-78fc-11e4-b4ce-a1001a8342c4", :last_name "Bergeron", :first_name "Andrew", :password "three333", :email "TheBerg@email.com", :id 3} {:token "09dbf5ec-78fc-11e4-b4ce-a1001a8342c4", :last_name "Bergeron", :first_name "Michael", :password "mypass333", :email "Mikeyyy@email.com", :id 4} {:token "09dcc1c0-78fc-11e4-b4ce-a1001a8342c4", :last_name "Cameron", :first_name "Kelsey", :password "password123", :email "kelsey@email.com", :id 5} {:token "09dcc3f0-78fc-11e4-b4ce-a1001a8342c4", :last_name "DeLoach", :first_name "Laura", :password "password123", :email "looloo@email.com", :id 6} {:token "09dd1652-78fc-11e4-b4ce-a1001a8342c4", :last_name "bob", :first_name "bill", :password "password123", :email "bob@email.com", :id 7} {:token "09dd18f0-78fc-11e4-b4ce-a1001a8342c4", :last_name "jo", :first_name "bobby", :password "password123", :email "jo@email.com", :id 8} {:token "09dd7980-78fc-11e4-b4ce-a1001a8342c4", :last_name "Norton", :first_name "Erin", :password "lolhaha3", :email "erin@email.com", :id 9} {:token "09dd7ae8-78fc-11e4-b4ce-a1001a8342c4", :last_name "Furr", :first_name "Carol", :password "password123", :email "cravycarol@email.com", :id 10} {:token "09ddc98a-78fc-11e4-b4ce-a1001a8342c4", :last_name "Albert", :first_name "Kelli", :password "lolhaha3", :email "kelli@email.com", :id 11} {:token "09ddcb42-78fc-11e4-b4ce-a1001a8342c4", :last_name "Ronco", :first_name "Savannah", :password "password123", :email "savanna@email.com", :id 12} {:token "09de21dc-78fc-11e4-b4ce-a1001a8342c4", :last_name "Clavelle", :first_name "Alex", :password "lolhaha3", :email "alex@email.com", :id 13} {:token "09de2380-78fc-11e4-b4ce-a1001a8342c4", :last_name "Clavelle", :first_name "Adam", :password "santalover", :email "adam@email.com", :id 14}])

(defn find-all
  "Using static data"
  ([table]
    (let [table (get {:items items-table
                      :groups groups-table
                      :wheels wheels-table
                      :users users-table} table)]
      table))
  ([table col val]
    (let [table (get {:items items-table
                      :groups groups-table
                      :wheels wheels-table
                      :users users-table} table)]
      (filter #(= (get % col) val) table))))

(defn find-one
  "Using static data"
  [table col val]
  (let [table (get {:items items-table
                    :groups groups-table
                    :wheels wheels-table
                    :users users-table} table)]
    (first (filter #(= (get % col) val) table))))

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

;;---------------------------
;; Item handlers

(defn items-index
  "Returns a Ring response containing every item in the db in JSON form."
  [request]
  (json-response (find-all :items)))

(defn items-show
  "Returns a Ring response containing a particular item specified by
  an id. If it exists, the data is returned in JSON form. Else, 404."
  [request]
  (if-let [item (find-one :items :id (Integer/parseInt (get-in request [:params :id])))]
    (json-response item)
    (ring/not-found "No item found with that id.")))

;;---------------------------
;; Group handlers

(defn groups-index
  "Returns a Ring response containing every group in the db in JSON form."
  [request]
  (json-response (find-all :groups)))

(defn groups-show
  "Returns a Ring response containing a particular group specified by
  an id. If it exists, the data is returned in JSON form. Else, 404."
  [request]
  (if-let [group (find-one :groups :id (Integer/parseInt (get-in request [:params :id])))]
    (json-response (assoc group :wheels (find-all :wheels :group_id (:id group))))
    (ring/not-found "No group found with that id.")))

;;---------------------------
;; Wheel handlers

(defn wheels-show
  "Returns a Ring response containing a particular wheel specified by
  an id. If it exists, the data is returned in JSON form. Else, 404."
  [request]
  (if-let [wheel (find-one :wheels :id (Integer/parseInt (get-in request [:params :id])))]
    (json-response (assoc wheel :items (find-all :items :wheel_id (:id wheel))))
    (ring/not-found "No wheel found with that id.")))

;;---------------------------
;; User handlers

(defn authenticate-user
  "Given a email and password, determines if the pair are valid credentials.
  If so, returns the user. Else, 403."
  [{:keys [body] :as request}]
  (let [{:keys [email password]} (json/decode (slurp body) true)
        user (find-one :users :email email)]
    (if (and user (= password (:password user)))
      (json-response (dissoc user :password))
      (unauthorized))))