(ns cloxy-http.server
  "This namespace contains definition of Jetty service"
  (:require [mount.core :as m]
            [cloxy-http.tools.conf :as c]
            [cloxy-http.routes :as r]
            [ring.adapter.jetty :as j]
            [ring.middleware.defaults :as d]
            [ring.logger.timbre :as l]
            [bidi.ring :as b]))

(defn- make-handler []
  (let [handler (-> r/routes
                    b/make-handler
                    (d/wrap-defaults
                      (assoc-in d/site-defaults [:security :anti-forgery] false)))]
    (if (:log-requests (c/handler))
      (l/wrap-with-logger handler)
      handler)))


(m/defstate server
            :start (j/run-jetty (make-handler) (c/jetty))
            :stop (.stop server))
