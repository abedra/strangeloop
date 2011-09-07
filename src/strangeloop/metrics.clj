(ns strangeloop.metrics
  (:use [incanter.charts :only (bar-chart)])
  (:require [strangeloop.twitter :as twitter]))

(defn words [data]
  (filter #(< 2 (count %))
          (re-seq #"\w+" (apply str (map :text data)))))

(defn freqs [words]
  {:words words
   :freqs (filter #(< 1 %)
                  (map (frequencies words) words))})

(defn word-chart [{:keys [words freqs]}]
  (bar-chart words freqs :vertical false))