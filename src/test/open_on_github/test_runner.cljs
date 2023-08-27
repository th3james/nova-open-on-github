(ns open-on-github.test-runner
  (:require
    [cljs.test :refer-macros [run-tests]]
    [open-on-github.commands-test]))


;; This isn't strictly necessary, but is a good idea depending
;; upon your application's ultimate runtime engine.
(enable-console-print!)


(defn run-all-tests
  []
  (run-tests 'open-on-github.commands-test))
