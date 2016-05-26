(ns ring-gunzip-middleware.core
  (:require [clojure.string :refer [lower-case]]))

(def lower-str (comp lower-case name))

(defn wrap-gunzip [handler]
  (fn [{:keys [body headers] :as request}]
    (if-let [content-type (some->> headers
                                   (map (fn [[k v]] [(lower-str k) v]))
                                   (get "content-encoding")
                                   lower-str
                                   #{"gzip"})]
      (handler (update request :body #(java.util.zip.GZIPInputStream. %)))
      (handler request))))
