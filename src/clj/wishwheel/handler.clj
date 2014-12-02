(ns wishwheel.handler
  (:require [wishwheel.dev :refer [browser-repl start-figwheel]]
            [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [wishwheel.api :as api]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [selmer.parser :refer [render-file]]
            [environ.core :refer [env]]
            [prone.middleware :refer [wrap-exceptions]]))

(defroutes routes
  (GET "/" [] (render-file "templates/index.html" {:dev (env :dev?)}))
  (GET "/api/items" []         api/items-index)
  (GET "/api/items/:id" []     api/items-show)
  (GET "/api/groups" []        api/groups-index)
  (GET "/api/groups/:id" []    api/groups-show)
  (GET "/api/wheels/:id" []    api/wheels-show)
  (POST "/api/authenticate" [] api/authenticate-user)
  (resources "/")
  (not-found "Not Found"))

(def app
  (let [handler (wrap-defaults routes api-defaults)]
    (if (env :dev?) (wrap-exceptions handler) handler)))
