(ns ring-gunzip-middlware.core
  (:require [clojure.string :refer [lower-case]]))

#_(defn gunzip
  [compressed]
  (with-open [in (java.util.zip.GZIPInputStream. compressed)]
    (slurp in)))

#_(defn gunzip-body
  [request]
  (update request :body gunzip))

(def lower-str (comp lower-case name))

(defn wrap-gunzip [handler]
  (fn [{:keys [body headers] :as request}]
    (if-let [content-type (some->> headers
                                   (map (fn [[k v]] [(lower-str k) v]))
                                   (get "content-type")
                                   lower-str
                                   #{"gzip"})]
      (handler (update request :body #(java.util.zip.GZIPInputStream. %)))
      (handler request))))
