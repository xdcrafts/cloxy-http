(ns cloxy-http.tools.logging
  (:require [mount.core :as m]
            [aero.core :as a]
            [taoensso.timbre :as t]
            [environ.core :as e]))

(def logging-configuration-source
  (or (e/env :logging-configuration-source)
      "resources/logging.edn"))

(def profile
  (or (e/env :profile)
      :default))

(defn configure-logging []
  (let [configuration (eval (a/read-config logging-configuration-source {:profile profile}))]
    (t/merge-config! configuration)
    configuration))

(configure-logging)

(m/defstate logging-conf :start (configure-logging)
            :stop {})
