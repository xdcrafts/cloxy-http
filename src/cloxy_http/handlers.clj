(ns cloxy-http.handlers
  (:require [mount.core :as mount]
            [cloxy-http.conf :as conf]
            [clj-http.core :as client]))

;; todo configure http client

(defn- re-route-request [req server-name server-port]
  (-> req
      (assoc :server-name server-name)
      (assoc :server-port server-port)))

(defn- prepare-multipart-headers [headers]
  (-> headers
      (dissoc "content-length")
      (dissoc "content-type")))

(defn- prepare-multipart-content [multipart-params]
  (->> multipart-params
       (map (fn [entry] {:name (first entry) :content (second entry)}))
       vec))

(defn delegate-handler-to [server-name server-port]
  (fn [req]
    (let [multipart (:multipart-params req)
          delegated-req-draft (re-route-request req server-name server-port)
          delegated-req (if (not (nil? multipart))
                          (let [multipart-content (prepare-multipart-content multipart)
                                headers (-> delegated-req-draft
                                            :headers
                                            prepare-multipart-headers)]
                            (-> delegated-req-draft
                                (assoc :multipart multipart-content)
                                (assoc :headers headers)))
                          (-> delegated-req-draft
                              (assoc :body (slurp (:body req)))))]
      (client/request delegated-req))))

(defn service-unavailable [req] {:status 503})

(mount/defstate delegate :start (delegate-handler-to (:host (conf/delegate)) (:port (conf/delegate)))
                         :stop service-unavailable)

(defn version [req] {:status 200
                     :headers {"Content-Type" "text/html"}
                     :body (System/getProperty "cloxy-http.version")})
