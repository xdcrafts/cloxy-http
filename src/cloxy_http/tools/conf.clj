(ns cloxy-http.tools.conf
  (:require [mount.core :as m]
            [environ.core :as e]
            [aero.core :as a]))

;; Core configuration

(def configuration-source
  (or (e/env :configuration-source)
      "resources/config.edn"))

(def profile
  (or (e/env :profile)
      :default))

(m/defstate conf :start (a/read-config configuration-source {:profile profile})
            :stop {})

;; Application-specific configuration

(defn jetty []
  (:jetty conf))

(defn proxy-to []
  (:proxy-to conf))

(defn delegate []
  (:delegate conf))

(defn handler []
  (:handler conf))

(defn http-client-connection-manager []
  (get-in conf [:http-client :connection-manager]))

(defn http-client-request []
  (get-in conf [:http-client :request]))
