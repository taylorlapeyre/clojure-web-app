(ns wishwheel.components.items)

(defn list-view
  [item]
  [:div {:class "item"}
   [:p [:strong (item "name")] " - " (item "price")]])