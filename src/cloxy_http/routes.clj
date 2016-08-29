(ns cloxy-http.routes
  "This namespace should require all handler functions that participates in routing"
  (:require [mount.core :as mount]
            [aero.core :as aero]
            [clojure.walk :as walk]
            [environ.core :as environ]
            [cloxy-http.handlers :as handlers]))

;; Core configuration

(def routes-source
  (or (environ/env :routes)
      "resources/routes.edn"))

(def profile
  (or (environ/env :profile)
      :default))

(mount/defstate routes
  :start
  (->> (aero/read-config routes-source {:profile profile})
       (walk/postwalk #(if (symbol? %)
                           (if-let [handler (resolve %)]
                             handler
                             (throw (Exception. (str "Unable to resolve handler:" %))))
                           %)))
  :stop
  ["/" [[true] handlers/service-unavailable]])


