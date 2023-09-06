(ns open-on-github.git-test
  (:require
    [cljs.core.async :refer [<! >! go]]
    [cljs.test :refer [async deftest is testing]]
    [clojure.string :as str]
    [open-on-github.git :refer [get-git-info get-branch build-github-url]]
    [open-on-github.test-helpers :refer [with-timeout]]))


(deftest test-get-git-info
  (testing "returns a map with the branch name"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor :fake-editor
                     fake-get-branch (fn [e]
                                       (is (= e fake-editor))
                                       (go "cool-branch"))]
                 (go
                   (let [r (<! (get-git-info fake-editor fake-get-branch))]
                     (is (= r {:branch "cool-branch"}))
                     (>! finished-chan :true)))))))))


(deftest test-get-branch
  (testing "runs a subprocess and returns the branch name"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor :fake-editor
                     fake-run-process (fn [executable args]
                                        (is (= executable "git"))
                                        (is (= args ["rev-parse" "--abbrev-ref" "HEAD"]))
                                        (go {:status 0 :out ["cool-branch\n"]}))]
                 (go
                   (let [r (<! (get-branch fake-editor fake-run-process))]
                     (is (= r "cool-branch"))
                     (>! finished-chan true)))))))))


(deftest test-build-github-url
  (testing "contains the branch name"
    (let [branch-name "main"
          git-info {:branch branch-name}
          result (build-github-url git-info)]
      (is (str/includes? result branch-name)))))
