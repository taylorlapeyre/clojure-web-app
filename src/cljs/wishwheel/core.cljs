(ns wishwheel.core
  "Functions for defining routes and kicking off application logic."
  (:require [reagent.core :as reagent :refer [atom]]
            [wishwheel.components :as components]
            [wishwheel.state :as state]
            [ajax.core :refer [GET]]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
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
  (letfn [(handler [response]
            (state/change-items! response)
            (state/change-current-page! :items-index))]
    (GET "/api/items" {:handler handler})))

(secretary/defroute items-show-path
  "/items/:id" [id]
  (letfn [(handler [response]
            (state/change-items! response)
            (state/change-current-page! :items-show))]
    (GET (str "/api/items/" id) {:handler handler})))

(secretary/defroute groups-index-path
  "/" []
  (letfn [(handler [response]
            (state/change-groups! response)
            (state/change-current-page! :groups-index))]
    (GET "/api/groups" {:handler handler})))

(secretary/defroute groups-show-path
  #"/groups/(\d+)" [id]
  (letfn [(handler [response]
            (state/change-groups! response)
            (state/change-current-page! :groups-show))]
    (GET (str "/api/groups/" id) {:handler handler})))

(secretary/defroute groups-new-path
  "/groups/new" []
  (if (state/gets :current-user)
    (state/change-current-page! :groups-new)
    (go-to "/")))

(secretary/defroute wheels-show-path
  "/wheels/:id" [id]
  (letfn [(handler [response]
            (state/change-wheels! response)
            (state/change-current-page! :wheels-show))]
    (GET (str "/api/wheels/" id) {:handler handler})))

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
