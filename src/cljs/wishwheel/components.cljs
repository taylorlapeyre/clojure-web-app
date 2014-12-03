(ns wishwheel.components
  "Functions that define React components.
  TODO: Reorganize this into multiple namespaces."
  (:require [wishwheel.state :as state]
            [ajax.core :refer [POST]]))

(defn go-to-path
  [url-path]
  (set! (.. js/window -location -href) url-path))

(defn go-to-item []
  "Gets the value of the input field for entering an item ID and
  sets the URL to the path to that item."
  (let [input-field (.getElementById js/document "id-field")
        id (.-value input-field)]
    (go-to-path (str "/#/items/" id))))

(defn attempt-to-sign-in!
  "Event handler for the sign in form's submit."
  [event]
  (.preventDefault event)
  (let [email    (.-value (.getElementById js/document "sign-in-email-field"))
        password (.-value (.getElementById js/document "sign-in-password-field"))
        handler #(do (go-to-path "/#")
                     (state/change-current-user! %))]
    (POST "/api/authenticate" {:params {:email email :password password}
                               :format :json
                               :response-format :json
                               :handler handler})))

(defn attempt-to-sign-up!
  "Event handler for the sign up form's submit."
  [event]
  (.preventDefault event)
  (let [email      (.-value (.getElementById js/document "sign-up-email-field"))
        password   (.-value (.getElementById js/document "sign-up-password-field"))
        first-name (.-value (.getElementById js/document "sign-up-first-name-field"))
        last-name  (.-value (.getElementById js/document "sign-up-last-name-field"))
        handler #(do (go-to-path "/#")
                     (state/change-current-user! %))]
    (POST "/api/users" {:params {:user {:email email
                                        :password password
                                        :first_name first-name
                                        :last_name last-name}}
                        :format :json
                        :response-format :json
                        :handler handler})))

(defn group-list-view
  [group]
  [:div {:class "group"}
   [:a {:href (str "#/groups/" (:id group))}
    [:strong (:name group)]]])

(defn item-list-view
  [item]
  [:div {:class "item"}
   [:p [:strong (:name item)] " - $" (:price item)]])

(defn user-state-box
  []
  [:p {:style {:float "right"}}
   (if-let [user (state/gets :current-user)]
     [:span "Signed In as: " (:email user) [:br]
      [:a {:on-click #(state/change-current-user! nil)} "Sign Out"]]
     [:span
      [:a {:href "#/signin"} "Sign In"] [:br]
      [:a {:href "#/signup"} "Sign Up"]])])

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
    (map item-list-view (state/gets :items))]])

(defmethod page :items-show [_]
  (when-let [item (state/gets :items)]
    [:div {:class "item-page"}
     [:h1 (:name item)]
     [:ul
      [:li "Price: $" (:price item)]
      [:li "ID: " (:id item)]]
     [:a {:href "#/"} "Back to Home"]]))

(defmethod page :groups-index [_]
  [:div
   [:h2 "All Groups"]
   [:div {:class "groups"}
    (map group-list-view (state/gets :groups))]])

(defmethod page :groups-show [_]
  (when-let [group (state/gets :groups)]
    [:div {:class "group-page"}
     [:h1 (:name group)]
     [:h3 "Wish Lists:"]
     [:ul
      (for [wheel (:wheels group)]
        [:li [:a {:href (str "#/wheels/" (:id wheel))}
              (:name wheel)]])]]))

(defmethod page :wheels-show [_]
  (when-let [wheel (state/gets :wheels)]
    [:div {:class "wheel-page"}
     [:h1 (:name wheel)]
     [:h3 "By "
      (get-in wheel [:user :first_name]) " "
      (get-in wheel [:user :last_name])]
     [:h3 "Gifts:"]
     [:ul
      (for [item (:items wheel)]
        [:li [:a {:href (str "#/items/" (:id item))}
              (:name item)]])]]))

(defmethod page :sign-in [_]
  [:div {:class "sign-in-page"}
   [:h2 "Sign In"]
   [:form {:on-submit attempt-to-sign-in!}
    [:input {:type "email" :placeholder "example@email.com" :id "sign-in-email-field"}] " "
    [:input {:type "password" :placeholder "Password" :id "sign-in-password-field"}] " "
    [:button {:type "submit"} "Sign In"]]])

(defmethod page :sign-up [_]
  [:div {:class "sign-up-page"}
   [:h2 "Sign Up"]
   [:form {:on-submit attempt-to-sign-up!}
    [:input {:type "email" :placeholder "example@email.com" :id "sign-up-email-field"}] " "
    [:input {:type "password" :placeholder "Password" :id "sign-up-password-field"}] " "
    [:input {:type "first_name" :placeholder "First" :id "sign-up-first-name-field"}] " "
    [:input {:type "last_name" :placeholder "Name" :id "sign-up-last-name-field"}] " "
    [:button {:type "submit"} "Sign Up"]]])

(defmethod page :default [_]
  [:div "Invalid/Unknown route"])

(defn main-page []
  [:div
    [user-state-box]
    [page (state/gets :current-page)]])