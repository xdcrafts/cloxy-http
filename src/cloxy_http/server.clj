(ns cloxy-http.server
  "This namespace contains definition of Jetty service"
  (:require [mount.core :as mount]
            [cloxy-http.conf :as conf]
            [cloxy-http.routes :as routes]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :as defaults]
            [bidi.ring :as bidi]))

(mount/defstate server
  :start (jetty/run-jetty
           (-> routes/routes
               bidi/make-handler
               (defaults/wrap-defaults
                 (assoc-in defaults/site-defaults [:security :anti-forgery] false)))
           (conf/jetty))
  :stop (.stop server))
