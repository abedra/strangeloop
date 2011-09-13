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
    [:div {:id "content"
           :style "width: 960px; margin: auto;"} body]]))

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
  (-> (twitter/fetch-public-timeline)
      (metrics/words)
      (metrics/freqs)
      (metrics/word-chart)
      (chart->bytes)
      (render-image)))

(defn cloud []
  (layout "Cloud"
          [:ul {:id "cloud" :style "list-style-type: none; width: 800px;"}
           (map (fn [word]
                  [:li {:class "cloud"
                        :style (str "font-size: "(second word) "em; display: block; float: left; padding-right: 20px;")}
                   (first word)])
                (metrics/cloud))]))

(defroutes routes
  (GET "/" [] (layout "Hello" "Strangeloop Clojure Analytics Workshop"))
  (GET "/chart" [] (create-chart))
  (GET "/cloud" [] (cloud)))

(def application
  (handler/site routes))

(defn start []
  (run-jetty (var application) {:port 8080
                                :join? false}))