(ns magnars.dev
  (:require [magnars.core :as magnars]
            [powerpack.dev :as dev :refer [reset]]
            [powerpack.export :as export]))

(defmethod dev/configure! :default []
  (magnars/create-app))

(defn start []
  (set! *print-namespace-maps* false)
  (dev/start))

(comment

  (start)
  (reset)

  (export/export (magnars/create-app))

  (dev/get-app)

  )
