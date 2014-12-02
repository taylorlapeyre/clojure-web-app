(ns wishwheel.core
    (:require [reagent.core :as reagent :refer [atom]]
              [wishwheel.components.pages :as page-components]
              [wishwheel.state :as state]
              [ajax.core :refer [GET]]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType])
    (:import goog.History))

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute items-index-path
  "/" []
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

;; -------------------------
;; Initialize app
(defn init! []
  (reagent/render-component [page-components/main-page]
                            (.getElementById js/document "app")))

;; -------------------------
;; History
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))
;; need to run this after routes have been defined
(hook-browser-navigation!)
