(ns wishwheel.components.pages
  (:require [wishwheel.state :as state]
            [ajax.core :refer [POST]]
            [wishwheel.components.items :as item-components]
            [wishwheel.components.groups :as group-components]))

(defn go-to-item []
  "Gets the value of the input field for entering an item ID and
  sets the URL to the path to that item."
  (let [input-field (.getElementById js/document "id-field")
        id (.-value input-field)]
    (set! (.. js/window -location -href) (str "/#/items/" id))))

(defn attempt-to-sign-in! [event]
  (.preventDefault event)
  (let [email (.-value (.getElementById js/document "email-field"))
        password (.-value (.getElementById js/document "password-field"))
        handler #(do (set! (.. js/window -location -href) "/")
                     (state/change-current-user! %))]
    (POST "/api/authenticate" {:params {:email email :password password}
                               :format :json
                               :handler handler})))

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
  [:div
   [:p {:style {:float "right"}}
    (if-let [user (state/gets :current-user)]
      [:span "Signed In as: " (:email (state/gets :current-user))
             [:a {:on-click #(state/change-current-user! nil)} "Sign Out"]]
      [:a {:href "#/signin"} "Sign In"])]

   [:h2 "All Groups"]
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

(defmethod page :sign-in [_]
  [:div {:class "Sign In Page"}
   [:h2 "Sign In"]
   [:form {:on-submit attempt-to-sign-in!}
    [:input {:type "email" :placeholder "example@email.com" :id "email-field"}] " "
    [:input {:type "password" :placeholder "Password" :id "password-field"}] " "
    [:button {:type "submit"} "Sign In"]]])

(defmethod page :default [_]
  [:div "Invalid/Unknown route"])

(defn main-page []
  [:div [page (state/gets :current-page)]])