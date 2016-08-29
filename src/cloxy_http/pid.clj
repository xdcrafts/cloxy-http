(ns cloxy-http.pid
  (:require [clojure.string :as string])
  (:import (java.lang.management ManagementFactory)
           (java.io File)))

(def pid
 (memoize (fn []
            (-> (ManagementFactory/getRuntimeMXBean)
                .getName
                (string/split #"@")
                first))))

(defn pid-file! [file-name]
  (let [^File pid-file (File. file-name)]
    (spit pid-file (pid))
    (.deleteOnExit pid-file)
    pid-file))

(defn pid-file-existance-checker [^File pid-file timeout shutdown-hook]
  (loop []
    (if (.exists pid-file)
      (do
        (Thread/sleep timeout)
        (recur))
      (shutdown-hook))))

(defn pid-file-watcher-thread
  ([file-name timeout]
   (pid-file-watcher-thread file-name timeout nil))
  ([file-name timeout shutdown-hook]
   (let [^File pid-file (pid-file! file-name)
         system-exit #(System/exit 0)
         callback (if (nil? shutdown-hook)
                    system-exit
                    #(do (shutdown-hook) (system-exit)))
         ^Thread thread (Thread. (fn [] (pid-file-existance-checker pid-file timeout callback)))]
     (doto thread
         (.setDaemon true)
         (.start))
     thread)))
