(ns wishwheel.core
  "Functions for defining routes and kicking off application logic."
  (:require [reagent.core :as reagent :refer [atom]]
            [wishwheel.components :as components]
            [wishwheel.state :as state]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [wishwheel.http :as http])
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:import goog.History))

(defn go-to
  "Change the current URL.
  FIXME: This is defined in components as well."
  [url-path]
  (set! (.. js/window -location -href) url-path))

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute items-index-path
  "/items" []
  (go
    (let [response (<! (http/GET "/api/items"))]
      (state/change-items! response)
      (state/change-current-page! :items-index))))

(secretary/defroute items-show-path
  "/items/:id" [id]
  (go
    (let [response (<! (http/GET (str "/api/items/" id)))]
      (state/change-items! response)
      (state/change-current-page! :items-show))))

(secretary/defroute groups-index-path
  "/" []
  (go
    (let [response (<! (http/GET "/api/groups"))]
      (state/change-groups! response)
      (state/change-current-page! :groups-index))))

(secretary/defroute groups-show-path
  #"/groups/(\d+)" [id]
  (go
    (let [response (<! (http/GET (str "/api/groups/" id)))]
      (state/change-groups! response)
      (state/change-current-page! :groups-show))))

(secretary/defroute groups-new-path
  "/groups/new" []
  (if (state/gets :current-user)
    (state/change-current-page! :groups-new)
    (go-to "/")))

(secretary/defroute wheels-show-path
  "/wheels/:id" [id]
  (go
    (let [response (<! (http/GET (str "/api/wheels/" id)))]
      (state/change-wheels! response)
      (state/change-current-page! :wheels-show))))

(secretary/defroute sign-in-path
  "/signin" [id]
  (state/change-current-page! :sign-in))

(secretary/defroute sign-up-path
  "/signup" []
  (state/change-current-page! :sign-up))

;; -------------------------
;;
(defn init!
  "This function kicks off the application."
  []
  (reagent/render-component [components/main-page]
                            (.getElementById js/document "app")))

;; -------------------------
;; History
(defn hook-browser-navigation!
  "Adds an event listener to the navigation event that triggers
  secretary's dispatch! function."
  []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))
;; need to run this after routes have been defined
(hook-browser-navigation!)
