(ns open-on-github.result-test
  (:require
    [cljs.test :refer [deftest is testing]]
    [open-on-github.result :refer [ok error unwrap]]))

(deftest test-unwrap-ok
  (testing "given an ok result returns the value"
    (is (= (unwrap (ok "cool")) "cool")))
  
  (testing "given an error result throws an error"
    (is (thrown-with-msg? js/Error #"Cannot unwrap error result: \"bad thing\"" (unwrap (error "bad thing")))))

  (testing "given an object that isn't a result throws an error"
    (is (thrown-with-msg? js/Error #"Cannot unwrap non-result: \"cool\"" (unwrap "cool"))))
  )


