(ns strangeloop.twitter
  (:use [clojure.data.json :only [read-json]])
  (:import (java.net URL)
           (java.io BufferedReader InputStreamReader)))

(defn- request
  "Fetches data from a given URL"
  [address]
  (let [url (URL. address)]
    (with-open [stream (. url (openStream))]
      (let [buf (BufferedReader. (InputStreamReader. stream))]
        (apply str (line-seq buf))))))

(defn- from-twitter
  []
  (read-json
   (request "http://twitter.com/statuses/public_timeline.json")))

(defn- from-file
  []
  (read-json
   (slurp "data/public_timeline.json")))

(defn fetch-public-timeline
  "Retrieves and parses the JSON formatted public timeline. If no internet connection
   is present or the public timeline cannot be parsed then pull it from a local file
   in the project."
  []
  (try
    (from-twitter)
    (catch Exception e
      (from-file))))

(defn print-public-timeline
  "Prints out the public timeline. Not really useful for anything but debugging.
   Use fetch-public-timeline for real results"
  []
  (doseq [tweet (fetch-public-timeline)]
    (println (str "@" (:screen_name (:user tweet))))
    (println (str (:text tweet) "\n"))))
