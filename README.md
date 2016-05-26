# ring-gunzip-middleware

Ring middleware for handling compressed HTTP bodies.

[![Clojars Project](https://img.shields.io/clojars/v/ring-gunzip-middlware.svg)]

```clojure
(:require [ring-gunzip-middleware.core :refer [wrap-gunzip]])

(defn handler
  [request]
  {:status 200
   :body (-> request :body slurp)})

(def app
  (-> handler
      wrap-gunzip))
```

## License

Copyright © 2016 mkwatson

Distributed under the Eclipse Public License version 1.0
