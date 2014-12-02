(ns wishwheel.components.pages
  (:require [wishwheel.state :as state]
            [wishwheel.components.items :as item-components]))

(defmulti page
  "Given a keyword, finds the method that implements that page and
  returns the Reagent page for it."
  identity)

(defn go-to-item []
  "Gets the value of the input field for entering an item ID and
  sets the URL to the path to that item."
  (let [input-field (.getElementById js/document "id-field")
        id (.-value input-field)]
    (set! (.. js/window -location -href) (str "/#/items/" id))))

(defmethod page :items-index [_]
  [:div [:h2 "All Items"]
   [:div
    "View Item with ID of: "
    [:input {:id "id-field" :type "number"}] " "
    [:button {:on-click go-to-item} "Go!"]]
   [:div {:class "items"}
    (map item-components/list-view (state/gets :items))]])

(defmethod page :items-show [_]
  (when-let [item (state/gets :items)]
    [:div {:class "item-page"}
     [:h1 (item "name")]
     [:ul
      [:li "Price: " (item "price")]
      [:li "ID: " (item "id")]]
     [:a {:href "#/"} "Back to Home"]]))

(defmethod page :default [_]
  [:div "Invalid/Unknown route"])

(defn main-page []
  [:div [page (state/gets :current-page)]])