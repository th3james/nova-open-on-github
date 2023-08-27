(ns open-on-github.test-runner
  (:require
    [cljs.test :refer-macros [run-tests]]
    [open-on-github.commands-test]
    [open-on-github.git-test]))


(enable-console-print!)


(defn run-all-tests
  []
  (run-tests 'open-on-github.commands-test
             'open-on-github.git-test))
