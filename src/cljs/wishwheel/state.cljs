(ns wishwheel.state
  "Functions for retreiving and updating local application state.
  State is destroyed on page reload."
  (:require [reagent.core :refer [atom]]
            [wishwheel.persist :as persist]))

; This is the only definition whose value changes.
(defonce app-state (atom {:current-user (persist/fetch ":current-user")}))

(defn gets
  "Get State. Retreives the given key from the app-state. Takes
  and optional default value."
  [k & [default]]
  (get @app-state k default))

(defn puts!
  "Put State. Potentially destructive, associates k with v in app-state."
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
  "Persists the given use data as :current-user and updates the state
  to reflect that."
  [user]
  (persist/store :current-user user)
  (puts! :current-user user))