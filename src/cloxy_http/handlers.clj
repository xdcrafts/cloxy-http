(ns cloxy-http.handlers
  (:require [mount.core :as mount]
            [cloxy-http.conf :as conf]
            [clj-http.core :as client]))

(defn service-unavailable [req] {:status 503})

;; todo configure http client

(defn delegate-handler-to [server-name server-port]
  (fn [req]
    (let [multipart (:multipart-params req)
          delegated-req-draft (-> req
                                  (assoc :server-name server-name)
                                  (assoc :server-port server-port))
          delegated-req (if (not (nil? multipart))
                          (let [multipart-content (->> multipart
                                                      (map (fn [entry] {:name (first entry) :content (second entry)}))
                                                      vec)
                                headers (-> (:headers delegated-req-draft)
                                            (dissoc "content-length")
                                            (dissoc "content-type"))]
                            (-> delegated-req-draft
                                (assoc :multipart multipart-content)
                                (assoc :headers headers)))
                          (-> delegated-req-draft
                              (assoc :body (slurp (:body req)))))]
      (client/request delegated-req))))

(mount/defstate delegate :start (delegate-handler-to (:host (conf/delegate)) (:port (conf/delegate)))
                         :stop service-unavailable)

(defn version [req] {:status 200
                     :headers {"Content-Type" "text/html"}
                     :body (System/getProperty "cloxy-http.version")})
