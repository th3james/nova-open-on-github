(ns open-on-github.core-test
  (:require
    [cljs.test :refer [deftest is run-tests]]))


(deftest simple-test
  (is (= 1 1)))


(run-tests)
