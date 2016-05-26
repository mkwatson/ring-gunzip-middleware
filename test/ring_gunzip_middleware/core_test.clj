(ns ring-gunzip-middleware.core-test
  (:require [clojure.test :refer :all]
            [ring-gunzip-middleware.core :refer :all]
            [cheshire.core :refer [parse-string]]
            [clojure.java.io :refer [input-stream output-stream]]))

;; (defn gzip
;;   [in]
;;   (let [baos (java.io.ByteArrayOutputStream.)]
;;     (with-open [out (-> baos
;;                         java.util.zip.GZIPOutputStream.
;;                         clojure.java.io/writer)]
;;       (.write out in))
;;     (let [out (.toByteArray baos)]
;;       (.close baos)
;;       out)))

(def uncompressed-input-path "resources/raw.json")
(def compressed-input-path "resources/raw.json.gz")
(def body (slurp uncompressed-input-path))

(defn wrap-errors
  [handler]
  (fn [request]
    (try (handler request)
         (catch Exception e
           {:status 400}))))

(defn handler
  [request]
  {:status 200
   :body (-> request :body slurp)})

(def app
  (-> handler
      wrap-gunzip
      wrap-errors))

(deftest functional-test
  
  (testing "correstly compressed"
    (is (= (app {:body (input-stream compressed-input-path)
                 :headers {"content-encoding" "gzip"}})
           {:status 200
            :body body
            :headers {"accept-encoding" "gzip"}})))

  (testing "not compressed"
    (is (= (app {:body (input-stream uncompressed-input-path)})
           {:status 200
            :body body
            :headers {"accept-encoding" "gzip"}})))

  (testing "failures"
    (is (= (app {:body (input-stream uncompressed-input-path)
                 :headers {"content-encoding" "gzip"}})
           {:status 400}))))
