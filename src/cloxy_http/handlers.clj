(ns cloxy-http.handlers
  (:require [mount.core :as m]
            [clj-http.core :as h]
            [cloxy-http.tools.conf :as c]
            [cloxy-http.tools.http.client.connection-manager :as cm]))

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

(defn- with-http-client-configuration [request]
  (-> request
      (into (c/http-client-request))
      (into {:connection-manager cm/connection-manager})))

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
                              (assoc :body (slurp (:body req)))))
          delegated-req-wtih-conf (with-http-client-configuration delegated-req)]
      (h/request delegated-req-wtih-conf))))

(defn service-unavailable [req] {:status 503})

(m/defstate delegate :start (delegate-handler-to (:host (c/delegate)) (:port (c/delegate)))
            :stop service-unavailable)

(defn version [req] {:status 200
                     :headers {"Content-Type" "text/html"}
                     :body (System/getProperty "cloxy-http.version")})
