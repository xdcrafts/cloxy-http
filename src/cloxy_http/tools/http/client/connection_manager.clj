(ns cloxy-http.tools.http.client.connection-manager
  (:require [mount.core :as m]
            [cloxy-http.tools.conf :as c]
            [clj-http.conn-mgr :as cm]))

(m/defstate connection-manager
            :start (cm/make-reusable-conn-manager (c/http-client-connection-manager))
            :stop nil)
