(ns open-on-github.result)


(defn ok
  [val]
  {:status :ok :val val})


(defn error
  [err]
  {:status :error :error err})
