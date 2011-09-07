(ns strangeloop.web
  (:use [ring.adapter.jetty :only (run-jetty)]
        [compojure.core :only (defroutes GET)]
        [incanter.core :only (save)])
  (:require [hiccup.core :as hiccup]
            [hiccup.page-helpers :as page]
            [compojure.handler :as handler]
            [strangeloop.twitter :as twitter]
            [strangeloop.metrics :as metrics])
  (:import (java.io ByteArrayInputStream ByteArrayOutputStream)))

(defn layout
  "The main layout for the web application"
  [title & body]
  (page/html5
   [:head
    [:title title]]
   [:body
    [:div {:id "header"} [:h2 "Hello Strangeloop"]]
    [:div {:id "content"} body]]))

(defn render-image
  "Returns the proper Ring response for an image"
  [bytes]
  {:status 200
   :headers {"Content-Type" "image/png"}
   :body bytes})

(defn chart->bytes
  "Produces an incanter chart as bytes for a web browser"
  [chart]
  (let [output-stream (ByteArrayOutputStream.)
        input-stream (do
                       (save chart output-stream)
                       (ByteArrayInputStream. (.toByteArray output-stream)))]
    input-stream))

(defn create-chart []
  (let [data (-> (twitter/fetch-public-timeline)
                 (metrics/words)
                 (metrics/freqs))]
    (render-image
     (chart->bytes
      (metrics/word-chart data)))))

(defroutes routes
  (GET "/" [] (layout "Hello" "Strangeloop Clojure Analytics Workshop"))
  (GET "/chart" [] (create-chart)))

(def application
  (handler/site routes))

(defn start []
  (run-jetty (var application) {:port 8080
                                :join? false}))