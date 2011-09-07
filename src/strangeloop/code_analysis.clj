;; This will be done at the REPL
(ns strangeloop.code-analysis
  (:use (incanter core stats charts)))

(defn complexity-plot []
  (let [complexity [34 78 339 63 54 72 80]
        plot (scatter-plot [1 2 3 4 5 6 7]
                           complexity
                           :title "Code Complexity" :x-label "Project" :y-label "Complexity")]
    (add-pointer plot 3 339 :text "Sample Project" :angle :sw)
    plot))

(defn code-coverage-plot []
  (let [coverage [98 76 80 99 89 20 92]
        plot (scatter-plot [1 2 3 4 5 6]
                           coverage
                           :title "Code Coverage" :x-label "Project" :y-label "Code Coverage")]
    (add-pointer plot 6 20 :text "Sample Project" :angle :nw)
    plot))