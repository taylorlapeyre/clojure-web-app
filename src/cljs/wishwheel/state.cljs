(ns wishwheel.state
  (:require [reagent.core :refer [atom]]
            [wishwheel.persist :as persist]))

(defonce app-state (atom {:current-user (persist/fetch ":current-user")}))

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
  (persist/store :current-user user)
  (puts! :current-user user))