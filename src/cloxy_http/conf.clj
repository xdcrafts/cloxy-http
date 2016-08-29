(ns cloxy-http.conf
  (:require [mount.core :as mount]
            [environ.core :as environ]
            [aero.core :as aero]))

;; Core configuration

(def configuration-source
  (or (environ/env :configuration-source)
      "resources/config.edn"))

(def profile
  (or (environ/env :profile)
      :default))

(mount/defstate conf :start (aero/read-config configuration-source {:profile profile})
                     :stop {})

;; Application-specific configuration

(defn jetty []
  (:jetty conf))

(defn proxy-to []
  (:proxy-to conf))

(defn delegate []
  (:delegate conf))
