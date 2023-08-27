(ns open-on-github.test-runner
  (:require
    [cljs.test :refer-macros [run-tests]]
    [open-on-github.commands-test]))


(enable-console-print!)


(defn run-all-tests
  []
  (run-tests 'open-on-github.commands-test))
