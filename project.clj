;; This is here to work with the stasis docker image, which bundles lein, but
;; not clojure.
(defproject magnars "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.xml "0.0.8"]
                 [clygments "2.0.2"]
                 [no.cjohansen/powerpack "2024.01.31"]]
  :jvm-opts ["-Djava.awt.headless=true"])
