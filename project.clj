(defproject strangeloop "1.0.0-SNAPSHOT"
  :description "Clojure analytics workshop example"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/data.json "0.1.1"]
                 [incanter "1.2.3" :exclusions [org.clojure/clojure]]
                 [compojure "0.6.5"]
                 [hiccup "0.3.6"]
                 [ring/ring-jetty-adapter "0.3.11"]]
  :dev-dependencies [[swank-clojure "1.3.0" :exclusions [org.clojure/clojure]]])
