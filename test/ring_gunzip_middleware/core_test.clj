(ns ring-gunzip-middleware.core-test
  (:require [clojure.test :refer :all]
            [ring-gunzip-middleware.core :refer :all]
            [cheshire.core :refer [generate-string]]
            [clojure.java.io :refer [input-stream output-stream]]))

(defn gzip
  [in]
  (let [baos (java.io.ByteArrayOutputStream.)]
    (with-open [out (-> baos
                        java.util.zip.GZIPOutputStream.
                        clojure.java.io/writer)]
      (.write out in))
    (let [out (.toByteArray baos)]
      (.close baos)
      out)))

(def body (generate-string {:greeting "Hello World"}))
(def raw-body (.getBytes body))
(def gz-body (gzip body))

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

(deftest compressed-bodies
  
  (testing "correstly compressed"
    (is (app {:body (input-stream gz-body)
              :headers {"Content-Encoding" "gzip"}})
        {:status 200
         :body body}))

  (testing "not compressed"
    (is (app {:body (input-stream raw-body)})
        {:status 200
         :body body}))

  (testing "failures"
    (is (app {:body (input-stream raw-body)
              :headers {"Content-Encoding" "gzip"}})
        {:status 400})))
