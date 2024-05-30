(ns magnars.export
  (:require [magnars.core :as magnars]
            [powerpack.export :as export]))

(defn ^:export export [& _args]
  (export/export! (magnars/create-app))
  (System/exit 0))
