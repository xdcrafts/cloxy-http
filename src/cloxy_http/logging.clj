(ns cloxy-http.logging
  (:require [mount.core :as mount]
            [aero.core :as aero]
            [taoensso.timbre :as timbre]
            [environ.core :as environ]))

(def logging-configuration-source
  (or (environ/env :logging-configuration-source)
      "resources/logging.edn"))

(def profile
  (or (environ/env :profile)
      :default))

(defn configure-logging []
  (let [configuration (eval (aero/read-config logging-configuration-source {:profile profile}))]
    (timbre/merge-config! configuration)
    configuration))

(configure-logging)

(mount/defstate logging-conf :start (configure-logging)
                             :stop {})
