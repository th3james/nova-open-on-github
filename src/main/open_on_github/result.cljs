(ns open-on-github.result)


(defn ok
  [val]
  {:status :ok :val val})


(defn error
  [err]
  {:status :error :error err})


(defn unwrap
  [result]
  (if (= (:status result) :ok)
    (:val result)
    (if (:error result)
      (throw (js/Error. (str "Cannot unwrap error result: \"" (:error result) "\"")))
      (throw (js/Error. (str "Cannot unwrap non-result: \"" result "\""))))))
