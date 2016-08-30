(ns cloxy-http.core
  (:gen-class)
  (:require [mount.core :as m]
            [cloxy-http.tools.logging :as l]
            [cloxy-http.tools.pid :as p]
            [cloxy-http.routes :as r]
            [cloxy-http.server :as s]))

(taoensso.timbre/refer-timbre)

(defn -main [& args]
  (info (m/start))
  (p/pid-file-watcher-thread ".pid" 5000 #(info (m/stop)))
  (info "System started"))
