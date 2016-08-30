(ns cloxy-http.routes
  "This namespace should require all handler functions that participates in routing"
  (:require [mount.core :as m]
            [aero.core :as a]
            [clojure.walk :as w]
            [environ.core :as e]
            [cloxy-http.handlers :as h]))

;; Core configuration

(def routes-source
  (or (e/env :routes)
      "resources/routes.edn"))

(def profile
  (or (e/env :profile)
      :default))

(m/defstate routes
            :start
            (->> (a/read-config routes-source {:profile profile})
             (w/postwalk #(if (symbol? %)
                              (if-let [handler (resolve %)]
                                handler
                                (throw (Exception. (str "Unable to resolve handler:" %))))
                              %)))
            :stop
            ["/" [[true] h/service-unavailable]])


