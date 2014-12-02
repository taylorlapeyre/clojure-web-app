(ns wishwheel.components.pages
  (:require [wishwheel.state :as state]
            [wishwheel.components.items :as item-components]
            [wishwheel.components.groups :as group-components]))

(defn go-to-item []
  "Gets the value of the input field for entering an item ID and
  sets the URL to the path to that item."
  (let [input-field (.getElementById js/document "id-field")
        id (.-value input-field)]
    (set! (.. js/window -location -href) (str "/#/items/" id))))

(defmulti page
  "Given a keyword, finds the method that implements that page and
  returns the Reagent component that will render it."
  identity)

(defmethod page :items-index [_]
  [:div [:h2 "All Items"]
   [:div "View Item with ID of: "
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

(defmethod page :groups-index [_]
  [:div [:h2 "All Groups"]
   [:div {:class "groups"}
    (map group-components/list-view (state/gets :groups))]])

(defmethod page :groups-show [_]
  (when-let [group (state/gets :groups)]
    [:div {:class "group-page"}
     [:h1 (group "name")]
     [:h3 "Wish Lists:"]
     [:ul
      (for [wheel (group "wheels")]
        [:li [:a {:href (str "#/wheels/" (wheel "id"))}
              (wheel "name")]])]]))

(defmethod page :wheels-show [_]
  (when-let [wheel (state/gets :wheels)]
    [:div {:class "wheel-page"}
     [:h1 (wheel "name")]
     [:h3 "Gifts:"]
     [:ul
      (for [item (wheel "items")]
        [:li [:a {:href (str "#/items/" (item "id"))}
              (item "name")]])]]))

(defmethod page :default [_]
  [:div "Invalid/Unknown route"])

(defn main-page []
  [:div [page (state/gets :current-page)]])