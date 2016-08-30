(ns cloxy-http.core
  (:gen-class)
  (:require [mount.core :as mount]
            [cloxy-http.logging :as l]
            [cloxy-http.routes :as r]
            [cloxy-http.server :as s]
            [cloxy-http.pid :as p]))

(taoensso.timbre/refer-timbre)

(defn -main [& args]
  (info (mount/start))
  (p/pid-file-watcher-thread ".pid" 5000 #(info (mount/stop)))
  (info "System started"))
