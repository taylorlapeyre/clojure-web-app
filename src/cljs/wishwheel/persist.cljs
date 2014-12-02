(ns wishwheel.persist
  "Functions for persisting data between sessions using some kind
  of storage mechanism.")

(defn store
  "Persist a certain value under k using localStorage."
  [k obj]
  (.setItem js/localStorage k (js/JSON.stringify (clj->js obj))))

(defn keywordify
  "Given a data structure, converts every identifying member
  of the collection to a keyword."
  [data]
  (cond
    (map? data) (into {} (for [[k v] data] [(keyword k) (keywordify v)]))
    (coll? data) (vec (map keywordify data))
    :else data))

(defn fetch
  "Retreives a value that exists in localStorage and return it
  as Clojurescript. Note that keys are always Strings."
  [k]
  (when-let [item (.getItem js/localStorage k)]
    (-> item
        (or (js-obj))
        (js/JSON.parse)
        (js->clj)
        (keywordify))))