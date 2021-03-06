(ns wishwheel.components
  "Functions that define React components.
  TODO: Reorganize this into multiple namespaces."
  (:require [wishwheel.state :as state]
            [wishwheel.http :as http])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn $ [selector] (.querySelector js/document selector))

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
  (go
    (let [data {:email (.-value ($ "#sign-in-email-field"))
                :password (.-value ($ "#sign-in-password-field"))}
          response (<! (http/POST "/api/authenticate" data))]
      (state/change-current-user! response)
      (go-to "/#"))))

(defn attempt-to-sign-up!
  "Event handler for the sign up form's submit."
  [event]
  (.preventDefault event)
  (go
    (let [data {:email      (.-value ($ "#sign-up-email-field"))
                :password   (.-value ($ "#sign-up-password-field"))
                :first_name (.-value ($ "#sign-up-first-name-field"))
                :last_name  (.-value ($ "#sign-up-last-name-field"))}
          response (<! (http/POST "/api/users" {:user data}))]
      (state/change-current-user! response)
      (go-to "/#"))))

(defn attempt-to-make-group!
  "Event handler for the form to create a new group."
  [event]
  (.preventDefault event)
  (go
    (let [data {:token (:token (state/gets :current-user))
                :group {:name (.-value ($ "#group-name-field"))}}
          response (<! (http/POST "/api/groups" data))]
      (state/change-groups! response)
      (go-to (str "/#/groups/" (:id (state/gets :groups)))))))

(defn attempt-to-add-user-to-group!
  [user group-id]
  (go
    (let [data {:token (:token (state/gets :current-user))
                :email (.-value ($ "#add-user-field"))}
          response (<! (http/POST (str "/api/groups/" group-id "/adduser") data))]
      (state/add-user-to-groups! response))))

(defn group-list-view
  [group]
  [:div {:class "group"}
    [:a {:href (str "#/groups/" (:id group))}
      [:strong (:name group)]]])

(defn item-list-view
  [item]
  [:div.item
    [:p [:strong (:name item)] " - $" (:price item)]])

(defn navbar
  []
  [:p.navbar
    [:a {:href "#/"} "All Groups"] " | "
    [:a {:href "#/items"} "All Items"] [:br]
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
      [:input#id-field {:type "number"}] " "
      [:button {:on-click go-to-item} "Go!"]]
    [:div.items
      (map item-list-view (state/gets :items))]])

(defmethod page :items-show [_]
  (when-let [item (state/gets :items)]
    [:div.item-page
      [:h1 (:name item)]
      [:ul
        [:li "Price: $" (:price item)]
        [:li "ID: " (:id item)]]
      [:a {:href "#/"} "Back to Home"]]))

(defmethod page :groups-index [_]
  [:div
    [:h2 "All Groups"]
    [:div.groups
      (map group-list-view (state/gets :groups))]
    (when (state/gets :current-user)
      [:a.button {:href "#/groups/new"} "Create a new Group"])])

(defmethod page :groups-show [_]
  (when-let [group (state/gets :groups)]
    [:div.group-page
      [:h1 (:name group)]

      [:h3 [:a {:href (str "#/groups/" (:id group) "/wheels/new")}
           "Create a new Wish Wheel in this Group."]]

      (when (= (:user_id group) (:id (state/gets :current-user)))
        [:p
          [:strong "Add a user to this Group:"]
          [:br]
          [:input#add-user-field {:type "email" :placeholder "another@email.com"}] " "
          [:button {:on-click #(attempt-to-add-user-to-group! % (:id group))} "Add User"]])

     [:h3 "Users in This Group:"]
      [:ul
        (for [user (:users group)]
          [:li (:email user)])]

     [:h3 "Wish Lists:"]
     [:ul
      (for [wheel (:wheels group)]
        [:li [:a {:href (str "#/wheels/" (:id wheel))} (:name wheel)]])]]))

(defmethod page :groups-new [_]
  [:div.groups-new
    [:h2 "Create a new Group"]
    [:form {:on-submit attempt-to-make-group!}
      [:input#group-name-field {:type "text" :placeholder "Christmas 2014"}] " "
      [:button {:type "submit"} "Create Group"]]])

(defmethod page :wheels-show [_]
  (when-let [wheel (state/gets :wheels)]
    [:div.wheel-page
      [:h1 (:name wheel)]
      [:h3 "By "
        (get-in wheel [:user :first_name]) " "
        (get-in wheel [:user :last_name])]
      [:h3 "Gifts:"]
      [:ul
        (for [item (:items wheel)]
          [:li
            [:a {:href (str "#/items/" (:id item))}
              (:name item)]])]]))

(defn item-form-view []
  [:form
    [:input#item-name]])

(defmethod page :wheels-new [_]
  (when-let [group (state/gets :groups)]
    [:div.wheels-new
      [:h2 "Create a new Wish Wheel"]
      [:form {:on-submit attempt-to-make-wheel!}
        [:input#wheel-name-field {:type "text" :placeholder "My Christmas List"}] " "
        [:button {:type "submit"} "Create Wish Wheel"]]]))

(defmethod page :sign-in [_]
  [:div.sign-in-page
    [:h2 "Sign In"]
    [:form {:on-submit attempt-to-sign-in!}
      [:input#sign-in-email-field {:type "email" :placeholder "example@email.com"}] " "
      [:input#sign-in-password-field {:type "password" :placeholder "Password"}] " "
      [:button {:type "submit"} "Sign In"]]])

(defmethod page :sign-up [_]
  [:div.sign-up-page
    [:h2 "Sign Up"]
    [:form {:on-submit attempt-to-sign-up!}
      [:input#sign-up-email-field {:type "email" :placeholder "example@email.com"}] " "
      [:input#sign-up-password-field {:type "password" :placeholder "Password"}] " "
      [:input#sign-up-first-name-field {:type "first_name" :placeholder "First"}] " "
      [:input#sign-up-last-name-field {:type "last_name" :placeholder "Name"}] " "
      [:button {:type "submit"} "Sign Up"]]])

(defmethod page :default [_]
  [:div "Invalid/Unknown route"])

(defn main-page []
  [:div
    [navbar]
    [page (state/gets :current-page)]])