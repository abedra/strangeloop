(ns strangeloop.main
  (:use [incanter.charts :only (bar-chart)])
  (:require [strangeloop.twitter :as twitter]))

(defn words []
  (filter #(< 2 (count %))
          (re-seq #"\w+"
                  (apply str
                         (map :text (twitter/fetch-public-timeline))))))

(defn freqs []
  (let [words (words)]
    {:words words
     :freqs (filter #(< 1 %)
                    (map (frequencies words) words))}))

(defn word-chart [{:keys [words freqs]}]
  (bar-chart words freqs :vertical false))