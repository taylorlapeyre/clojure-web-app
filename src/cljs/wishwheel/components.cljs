(ns wishwheel.components
  "Functions that define React components.
  TODO: Reorganize this into multiple namespaces."
  (:require [wishwheel.state :as state]
            [ajax.core :refer [POST]]))

(defn $ [selector] (.querySelector js/document selector))

(defn ajax-post
  [url data handler]
  (POST url {:params data
             :format :json
             :response-format :json
             :handler handler}))

(defn go-to
  [url-path]
  (set! (.. js/window -location -href) url-path))

(defn go-to-item []
  "Gets the value of the input field for entering an item ID and
  sets the URL to the path to that item."
  (let [input-field ($ "#id-field")
        id (.-value input-field)]
    (go-to (str "/#/items/" id))))

(defn attempt-to-sign-in!
  "Event handler for the sign in form's submit."
  [event]
  (.preventDefault event)
  (let [email    (.-value ($ "#sign-in-email-field"))
        password (.-value ($ "#sign-in-password-field"))
        handler #(do (go-to "/#")
                     (state/change-current-user! %))]
    (ajax-post "/api/authenticate" {:email email :password password} handler)))

(defn attempt-to-sign-up!
  "Event handler for the sign up form's submit."
  [event]
  (.preventDefault event)
  (let [email      (.-value ($ "#sign-up-email-field"))
        password   (.-value ($ "#sign-up-password-field"))
        first-name (.-value ($ "#sign-up-first-name-field"))
        last-name  (.-value ($ "#sign-up-last-name-field"))
        handler #(do (go-to "/#")
                     (state/change-current-user! %))]
    (ajax-post "/api/users" {:user {:email email
                                    :password password
                                    :first_name first-name
                                    :last_name last-name}} handler)))

(defn attempt-to-make-group!
  "Event handler for the form to create a new group."
  [event]
  (.preventDefault event)
  (let [name (.-value ($ "#group-name-field"))
        token (:token (state/gets :current-user))
        handler #(do (state/change-groups! %)
                     (go-to (str "/#/groups/" (:id (state/gets :groups)))))]
    (ajax-post "/api/groups" {:token token
                              :group {:name name}} handler)))

(defn attempt-to-add-user-to-group!
  [user group-id]
  (let [email (.-value ($ "#add-user-field"))
        token (:token (state/gets :current-user))
        handler state/add-user-to-groups!]
    (ajax-post (str "/api/groups/" group-id "/adduser") {:token token
                                                         :email email} handler)))

(defn group-list-view
  [group]
  [:div {:class "group"}
   [:a {:href (str "#/groups/" (:id group))}
    [:strong (:name group)]]])

(defn item-list-view
  [item]
  [:div {:class "item"}
   [:p [:strong (:name item)] " - $" (:price item)]])

(defn navbar
  []
  [:p {:class "navbar"}
   [:a {:href "#/"} "All Groups"] " | "
   [:a {:href "#/items"} "All Items"] [:br]
   (if-let [user (state/gets :current-user)]
     [:span "Signed In as: " (:email user) [:br]
      [:a {:on-click #(state/change-current-user! nil)} "Sign Out"]]
     [:span
      [:a {:href "#/signin"} "Sign In"] [:br]
      [:a {:href "#/signup"} "Sign Up"]])
   [:hr]])

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
    (map group-list-view (state/gets :groups))]
   (when (state/gets :current-user)
     [:a {:href "#/groups/new" :class "button"} "Create a new Group"])])

(defmethod page :groups-show [_]
  (when-let [group (state/gets :groups)]
    [:div {:class "group-page"}
     [:h1 (:name group)]
     (when (= (:user_id group) (:id (state/gets :current-user)))
       [:p [:strong "Add a user to this Group:"]
        [:br]
        [:input {:type "email" :placeholder "another@email.com" :id "add-user-field"}] " "
        [:button {:on-click #(attempt-to-add-user-to-group! % (:id group))} "Add User"]])
     [:h3 "Users in This Group:"]
      [:ul
       (for [user (:users group)]
         [:li (:email user)])]
     [:h3 "Wish Lists:"]
     [:ul
      (for [wheel (:wheels group)]
        [:li [:a {:href (str "#/wheels/" (:id wheel))}
              (:name wheel)]])]]))

(defmethod page :groups-new [_]
  [:div {:class "groups-new"}
   [:h2 "Create a new Group"]
   [:form {:on-submit attempt-to-make-group!}
    [:input {:type "text" :placeholder "Christmas 2014" :id "group-name-field"}] " "
    [:button {:type "submit"} "Create Group"]]])

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
    [navbar]
    [page (state/gets :current-page)]])