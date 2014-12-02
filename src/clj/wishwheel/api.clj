(ns wishwheel.api
  (:require [ring.util.response :as ring]
            [oj.core :as oj]
            [cheshire.core :as json]))

;;---------------------------
;; "database" stuff

(def db {:subprotocol "mysql"
         :subname "//127.0.0.1:3306/wishwheel3"
         :user "root"
         :password ""})

(defn find-all
  [table]
  (oj/exec {:table table} db))

(defn find-one
  [table col val]
  (first (oj/exec {:table table
                   :where {col val}} db)))

;;---------------------------
;; HTTP helpers

(defn json-response
  "Takes a Clojure data structure and returns a Ring HTTP response containing
  the data in json form."
  [data]
  (-> (json/encode data)
      (ring/response)
      (ring/content-type "application/json")))

;;---------------------------
;; Item handlers

(defn items-index
  "Gets all items in the database."
  [request]
  (json-response (find-all :items)))

(defn items-show
  "Get a particular item from the database. Expects a :name param key."
  [request]
  (let [item (find-one :items :id (get-in request [:params :id]))]
    (if item
      (json-response item)
      (ring/not-found "No item found with that name."))))

;(defn items#create
;  "Create a new item. Expects a :item body key."
;  (let [data (json/decode (get-in request [:body :items]))]
;    ))