(ns open-on-github.test-helpers
  (:require
    [cljs.core.async :refer [alts! chan go timeout]]
    [cljs.test :refer [is]]))


(defn with-timeout
  [done test-fn]
  (let [timeout-chan (timeout 100)
        finished-chan (chan 1)]
    (test-fn finished-chan)
    (go
      (let [[_ emitting-chan] (alts! [timeout-chan finished-chan])]
        (if (= emitting-chan finished-chan)
          (done)
          (is false "Test timed out"))))))
