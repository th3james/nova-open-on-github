(ns open-on-github.path-test
  (:require
    [cljs.test :refer [deftest is testing]]
    [open-on-github.path :refer [chroot]]))


(deftest test-chroot
  (testing "given a valid parent path"
    (let [parent-path "/User/cool-guy/src/hat"
          file-path "/User/cool-guy/src/hat/nice/robot.txt"]
      (testing "it succeeds"
        (is (= (:status (chroot file-path parent-path)) :ok)))
      (testing "it returns the chroot"
        (is (= (:val (chroot file-path parent-path)) "nice/robot.txt")))))

  (testing "given an invalid parent path"
    (let [parent-path "/nope"
          file-path "/Other/thing.md"]
      (testing "it fails"
        (is (= (:status (chroot file-path parent-path)) :error)))
      (testing "it returns an error message"
        (is (= (:error (chroot file-path parent-path)) "Cannot chroot '/Other/thing.md' to '/nope'"))))))
