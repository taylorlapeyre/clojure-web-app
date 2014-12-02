(ns wishwheel.state
  (:require [reagent.core :as reagent :refer [atom]]))

(defn store
  [k obj]
  (.setItem js/localStorage k (js/JSON.stringify (clj->js obj))))

(defn keywordify [m]
  (cond
    (map? m) (into {} (for [[k v] m] [(keyword k) (keywordify v)]))
    (coll? m) (vec (map keywordify m))
    :else m))

(defn fetch
  [k]
  (when-let [item (.getItem js/localStorage k)]
    (-> item
        (or (js-obj))
        (js/JSON.parse)
        (js->clj)
        (keywordify))))

(defonce app-state (atom {:current-user (fetch ":current-user")}))

(defn gets
  [k & [default]]
  (get @app-state k default))

(defn puts!
  [k v]
  (swap! app-state assoc k v))

(defn change-items!
  [items]
  (puts! :items items))

(defn change-groups!
  [groups]
  (puts! :groups groups))

(defn change-wheels!
  [wheels]
  (puts! :wheels wheels))

(defn change-current-page!
  [page]
  (puts! :current-page page))

(defn change-current-user!
  [user]
  (store :current-user user)
  (puts! :current-user user))