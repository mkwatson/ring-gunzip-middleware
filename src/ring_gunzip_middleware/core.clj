(ns ring-gunzip-middleware.core
  (:require [clojure.string :refer [lower-case]]))

(defn wrap-accept-encoding
  [response]
  (update response :headers (fnil #(conj % ["accept-encoding" "gzip"]) {})))

(defn wrap-gunzip [handler]
  (fn [{headers :headers :as request}]
    (if-let [content-encoding (some-> headers (get "content-encoding") #{"gzip"})]
      (wrap-accept-encoding (handler (update request :body #(java.util.zip.GZIPInputStream. %))))
      (wrap-accept-encoding (handler request)))))
