(ns wishwheel.api
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
  "Using the database"
  ([table]
    (oj/exec {:table table} db))
  ([table col val]
    (oj/exec {:table table
              :where {col val}} db)))

(defn find-one
  "Using the database"
  [table col val]
  (first (oj/exec {:table table
                   :where {col val}} db)))


;;---------------------------
;; Fake database stuff

(def items-table  [{:user_id nil, :wheel_id 1, :image "/public/images/system/sdh324kj.png", :price 15.00M, :name "Rubix Cube", :id 1} {:user_id nil, :wheel_id 1, :image "/public/images/system/sdfl346.png", :price 600.00M, :name "Volcano", :id 2} {:user_id nil, :wheel_id 1, :image "/public/images/system/lmhjf46.png", :price 86.00M, :name "Ear Rings", :id 3} {:user_id nil, :wheel_id 2, :image "/public/images/system/rthfhg4.png", :price 23.00M, :name "Stir Crazy", :id 4} {:user_id nil, :wheel_id 2, :image "/public/images/system/iuh434.png", :price 299.00M, :name "iPhone 6", :id 5} {:user_id nil, :wheel_id 3, :image "/public/images/system/ps4.png", :price 299.00M, :name "Playstation 4", :id 6} {:user_id nil, :wheel_id 3, :image "/public/images/system/purp_nugs.png", :price 300.00M, :name "Purple Erkle", :id 7} {:user_id nil, :wheel_id 3, :image "/public/images/system/rolex.png", :price 5000.00M, :name "Rolex", :id 8} {:user_id nil, :wheel_id 3, :image "/public/images/system/xbox1.png", :price 349.00M, :name "Xbox One", :id 9} {:user_id nil, :wheel_id 3, :image "/public/images/system/ipad3.png", :price 299.00M, :name "iPad", :id 10} {:user_id nil, :wheel_id 3, :image "/public/images/system/htcMe.png", :price 599.00M, :name "HTC One M8", :id 11} {:user_id nil, :wheel_id 5, :image "/public/images/system/sdfsdfpph3244kj.png", :price 780.00M, :name "David Yurman Bracelet", :id 12} {:user_id nil, :wheel_id 5, :image "/public/images/system/ssdfdfl348396.png", :price 40.00M, :name "Pandora Charm", :id 13} {:user_id nil, :wheel_id 5, :image "/public/images/system/lmhsdfjf46r.png", :price 87.00M, :name "Fossil Watch", :id 14} {:user_id nil, :wheel_id 6, :image "/public/images/system/ksdk34.png", :price 67.00M, :name "Hammock", :id 15} {:user_id nil, :wheel_id 6, :image "/public/images/system/kjfiw984.png", :price 35.00M, :name "Chromecast", :id 16} {:user_id nil, :wheel_id 6, :image "/public/images/system/4iw4.png", :price 12.00M, :name "White Beanie", :id 17} {:user_id nil, :wheel_id 7, :image "/public/images/system/sdh324kj.png", :price 16.00M, :name "Rubix Cube", :id 18} {:user_id nil, :wheel_id 7, :image "/public/images/system/sdfl346.png", :price 600.00M, :name "Volcano", :id 19} {:user_id nil, :wheel_id 7, :image "/public/images/system/lmhjf46.png", :price 86.00M, :name "Ear Rings", :id 20} {:user_id nil, :wheel_id 8, :image "/public/images/system/rthfhg4.png", :price 23.00M, :name "Stir Crazy", :id 21} {:user_id nil, :wheel_id 8, :image "/public/images/system/iuh434.png", :price 299.00M, :name "iPhone 6", :id 22} {:user_id nil, :wheel_id 9, :image "/public/images/system/zxc45j.png", :price 8.00M, :name "Notebook", :id 23} {:user_id nil, :wheel_id 9, :image "/public/images/system/s96.png", :price 100.00M, :name "Mechanical Keyboard", :id 24} {:user_id nil, :wheel_id 10, :image "/public/images/system/uiodk67.png", :price 12.00M, :name "Wine Glass", :id 25} {:user_id nil, :wheel_id 10, :image "/public/images/system/isw854.png", :price 6.00M, :name "Sharpie", :id 26} {:user_id nil, :wheel_id 11, :image "/public/images/system/zxc4yyy5j.png", :price 4.00M, :name "Butterfinger Bites", :id 27} {:user_id nil, :wheel_id 11, :image "/public/images/system/s96yy8.png", :price 999.00M, :name "Creative Cloud", :id 28} {:user_id nil, :wheel_id 12, :image "/public/images/system/uioyyydk7.png", :price 52.00M, :name "Kindle", :id 29} {:user_id nil, :wheel_id 12, :image "/public/images/system/isw8554q4.png", :price 3.00M, :name "Andriod Charger", :id 30} {:user_id nil, :wheel_id 11, :image "/public/images/system/zrt8j.png", :price 500.00M, :name "50in TV", :id 31} {:user_id nil, :wheel_id 11, :image "/public/images/system/kdu2.png", :price 450.00M, :name "Xbox One", :id 32} {:user_id nil, :wheel_id 12, :image "/public/images/system/upsdj2.png", :price 93.00M, :name "Golf club", :id 33} {:user_id nil, :wheel_id 12, :image "/public/images/system/kdns11.png", :price 88.00M, :name "Raybans", :id 34}])
(def groups-table [{:user_id 1, :name "Christmas 2014", :id 1} {:user_id 3, :name "Bergeron Christmas", :id 2} {:user_id 5, :name "Girls' Christmas 2014", :id 3} {:user_id 7, :name "super christmas", :id 4} {:user_id 9, :name "Roommate Christmas 2k14", :id 5} {:user_id 11, :name "12/25/14", :id 6} {:user_id 13, :name "Brother bonding", :id 7}])
(def wheels-table [{:user_id 1, :group_id 1, :name "Taylor's Christmas List", :id 1} {:user_id 2, :group_id 1, :name "Jeff's Christmas List", :id 2} {:user_id 3, :group_id 2, :name "Andrew’s Christmas List", :id 3} {:user_id 4, :group_id 2, :name "Bergeron’s Christmas List", :id 4} {:user_id 5, :group_id 3, :name "Kelsey's Christmas List", :id 5} {:user_id 6, :group_id 3, :name "Laura's Christmas List", :id 6} {:user_id 7, :group_id 4, :name "Taylor's Christmas List", :id 7} {:user_id 8, :group_id 4, :name "Jeff's Christmas List", :id 8} {:user_id 9, :group_id 4, :name "Erin's Christmas List", :id 9} {:user_id 10, :group_id 4, :name "Carol's Christmas List", :id 10} {:user_id 11, :group_id 5, :name "Kelli's Christmas List", :id 11} {:user_id 12, :group_id 5, :name "Savannah's Christmas List", :id 12} {:user_id 13, :group_id 6, :name "Alex's Christmas List", :id 13} {:user_id 14, :group_id 6, :name "Adam's Christmas List", :id 14}])

(defn find-all
  "Using static data"
  ([table]
    (let [table (get {:items items-table
                      :groups groups-table
                      :wheels wheels-table} table)]
      table))
  ([table col val]
    (let [table (get {:items items-table
                    :groups groups-table
                    :wheels wheels-table} table)]
      (filter #(= (get % col) val) table))))

(defn find-one
  "Using static data"
  [table col val]
  (let [table (get {:items items-table
                    :groups groups-table
                    :wheels wheels-table} table)]
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

;;---------------------------
;; Item handlers

(defn items-index
  "Gets all items in the database."
  [request]
  (json-response (find-all :items)))

(defn items-show
  "Get a particular item from the database. Expects an :id params key."
  [request]
  (let [item (find-one :items :id (Integer/parseInt (get-in request [:params :id])))]
    (if item
      (json-response item)
      (ring/not-found "No item found with that id."))))

;;---------------------------
;; Group handlers

(defn groups-index
  "Gets all groups in the database."
  [request]
  (json-response (find-all :groups)))

(defn groups-show
  "Get a particular item from the database. Expects an :id params key."
  [request]
  (let [group (find-one :groups :id (Integer/parseInt (get-in request [:params :id])))]
    (if group
      (json-response (assoc group :wheels (find-all :wheels :group_id (:id group))))
      (ring/not-found "No group found with that id."))))

;;---------------------------
;; Wheel handlers

(defn wheels-show
  "Get a particular item from the database. Expects an :id params key."
  [request]
  (let [wheel (find-one :wheels :id (Integer/parseInt (get-in request [:params :id])))]
    (if wheel
      (json-response (assoc wheel :items (find-all :items :wheel_id (:id wheel))))
      (ring/not-found "No wheel found with that id."))))