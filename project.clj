(defproject ring-gunzip-middleware "0.1.3"
  :description "Ring middleware for handling compressed POST/PUT body"
  :url "https://github.com/mkwatson/ring-gunzip-middleware"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :author "mkwatson"
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :profiles {:local-test {:dependencies [[cheshire "5.6.1"]]}}
  :aliases {"test" ["with-profile" "local-test" "test"]
            "test-repl" ["with-profile" "local-test" "repl"]})
