(ns wishwheel.persist)

(defn store
  [k obj]
  (.setItem js/localStorage k (js/JSON.stringify (clj->js obj))))

(defn keywordify [m]
  (cond
    (map? m) (into {} (for [[k v] m] [(keyword k) (keywordify v)]))
    (coll? m) (vec (map keywordify m))
    :else m))

(defn fetch
  [k]
  (when-let [item (.getItem js/localStorage k)]
    (-> item
        (or (js-obj))
        (js/JSON.parse)
        (js->clj)
        (keywordify))))