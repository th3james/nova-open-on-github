(ns open-on-github.git-test
  (:require
    [cljs.core.async :refer [go timeout alts!]]
    [cljs.test :refer [async deftest is testing]]
    [open-on-github.git :refer [get-git-info]]))


(deftest test-get-git-info
  (testing "returns a map with the branch name"
    (async done
           (let [t (timeout 100)
                 fake-editor :fake-editor
                 fake-get-branch (fn [e]
                                   (is (= e fake-editor))
                                   (go "cool-branch"))
                 r (get-git-info fake-editor fake-get-branch)]
             (go
               (let [[v ch] (alts! [t r])]
                 (if (= ch r)
                   (do
                     (is (= v {:branch "cool-branch"}))
                     (done))
                   (is false "Timeout reached, test failed"))))))))
