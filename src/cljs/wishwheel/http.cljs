(ns wishwheel.http
  "Thin wrappers over cljs-ajax to enable writing in a core.async style."
  (:require [ajax.core :as ajax]
            [cljs.core.async :refer [chan put!]]))

(defn GET
  [url]
  (let [c (chan 1)]
    (ajax/GET url {:format :json
                   :response-format :json
                   :handler #(put! c %)})
    c))

(defn POST
  [url data]
  (let [c (chan 1)]
    (ajax/POST url {:params data
                    :format :json
                    :response-format :json
                    :handler #(put! c %)})
    c))