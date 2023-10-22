(ns open-on-github.path-test
  (:require
    [cljs.test :refer [deftest is testing]]
    [open-on-github.path :refer [chroot]]))

(deftest test-chroot
  (testing "returns the path with the given chroot"
    (is (= (chroot "/User/cool-guy/src/hat/nice/robot.txt" "/User/cool-guy/src/hat") "nice/robot.txt"))))
