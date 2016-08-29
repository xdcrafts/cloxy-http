(ns cloxy-http.core
  (:gen-class)
  (:require [mount.core :as mount]
            [cloxy-http.routes :as r]
            [cloxy-http.server :as s]
            [cloxy-http.pid :as p]))

(defn -main [& args]
  (println (mount/start))
  (p/pid-file-watcher-thread ".pid" 5000 #(println (mount/stop)))
  (println "System started"))
