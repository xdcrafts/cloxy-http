(def version "0.1.0-SNAPSHOT")

(defproject cloxy-http version
  :description "Project template that aims to left all legacy stuff behind and bring new functionality iteratively."
  :url "https://github.com/xdcrafts/cloxy-http"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main cloxy-http.core
  :plugins [[lein-environ "1.1.0"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.namespace "0.2.11" :scope "provided"]
                 [mount "0.1.10"]
                 [environ "1.1.0"]
                 [aero "1.0.0"]
                 [clj-http "3.2.0"]
                 [ring/ring-core  "1.5.0"]
                 [ring/ring-defaults  "0.2.1"]
                 [ring/ring-jetty-adapter "1.5.0"]
                 [bidi "2.0.9"]])
