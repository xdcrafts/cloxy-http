(ns cloxy-http.server
  "This namespace contains definition of Jetty service"
  (:require [mount.core :as mount]
            [cloxy-http.conf :as conf]
            [cloxy-http.routes :as routes]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :as defaults]
            [ring.logger.timbre :as logger]
            [bidi.ring :as bidi]))

(defn- make-handler []
  (let [handler (-> routes/routes
                    bidi/make-handler
                    (defaults/wrap-defaults
                      (assoc-in defaults/site-defaults [:security :anti-forgery] false)))]
    (if (:log-requests (conf/handler))
      (logger/wrap-with-logger handler)
      handler)))


(mount/defstate server
  :start (jetty/run-jetty (make-handler) (conf/jetty))
  :stop (.stop server))
