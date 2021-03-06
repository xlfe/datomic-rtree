(ns user
  (:require
            [clojure.java.javadoc :refer (javadoc)]
            [clojure.pprint :refer (pprint print-table pp)]
            [clojure.reflect :refer (reflect)]
            [clojure.repl :refer (apropos dir doc find-doc pst source)]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.test :as test]
            [clojure.tools.namespace.repl :refer (refresh refresh-all)]
            [clojure.tools.trace :refer (trace deftrace trace-forms trace-ns trace-vars)]
            [datomic.api :refer (q db) :as d]
            [dali.svg-translate :as dali]
            [meridian.datomic-rtree
             [rtree :as rtree]
             [test-utils :as utils]
             [bbox :as bbox]
             [bulk :as bulk]
             [shapes :as shapes]
             [hilbert :as hilbert]]))

(defn reset [] (refresh))

(defn run-all-my-tests []
  (reset)
  (test/run-all-tests #"meridian.datomic-rtree.*-test$"))

#_ (def conn (d/connect "datomic:mem://rtrees"))

(defn retrieve-all-points [conn]
  (->> (d/q '[:find ?e :where [?e :type :Point]] (db conn))
       (map #(d/entity (db conn) (first %)))
       (map #(into {} (seq %)))
       (map (juxt :x :y))))

(defn plot-svg-points [points filename]
  (let [max-x (reduce max (map first points))
        max-y (reduce max (map second points))
        svg [:page {:height (+ max-y 10) :width (+ max-x 10) :stroke {:paint :black :width 1} :fill :none}
             (map (fn [[x y]] [:circle [x y] 2]) points)]]
    (-> svg dali/dali->hiccup (dali/spit-svg filename))))

(defn tree-depth
  "Discover the depth of the rtree by taking a random path to the bottom"
  [db]
  (loop [node (:rtree/root (rtree/find db))
         depth 0]
    (let [children (:node/children node)]
      (if (empty? children)
        depth
        (recur (rand-nth (vec children))
               (inc depth))))))

(comment
  (let [points (take 3000 (repeatedly #(vector (rand-int 1000) (rand-int 1000))))]
    (spit "resources/test-data/search-points.edn" (with-out-str (pprint points)))
    (plot-svg-points points "/var/www/points.svg")))
