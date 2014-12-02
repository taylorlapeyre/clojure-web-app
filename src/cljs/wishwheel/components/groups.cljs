(ns wishwheel.components.groups)

(defn list-view
  [group]
  [:div {:class "group"}
   [:a {:href (str "#/groups/" (group "id"))}
    [:strong (group "name")]]])