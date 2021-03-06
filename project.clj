(defproject org.clojars.sideris/datomic-rtree "0.1.1-SNAPSHOT"
  :description "An r-tree based spatial indexer for Datomic. Fork of https://github.com/stathissideris/datomic-rtree"
  :url "https://github.com/stathissideris/datomic-rtree"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.datomic/datomic-free "0.9.4752"]
                 [quil "1.6.0"]
                 [dali "0.4.0"]
                 [meridian/clj-jts "0.0.2"]]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.trace "0.7.6"]
                                  [org.clojure/tools.namespace "0.2.4"]]}})
