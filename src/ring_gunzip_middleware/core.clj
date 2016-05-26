(ns ring-gunzip-middleware.core
  (:require [clojure.string :refer [lower-case]]))

(defn wrap-gunzip [handler]
  (fn [{headers :headers :as request}]
    (let [{content-encoding "content-encoding"
           content-length "content-length"} headers]
      (if (and content-length
               (#{"gzip"} content-encoding))
        (handler (update request :body #(java.util.zip.GZIPInputStream. %)))
        (handler request)))))
