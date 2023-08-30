(ns open-on-github.git-test
  (:require
    [cljs.core.async :refer [go timeout alts!]]
    [cljs.test :refer [async deftest is testing]]
    [clojure.string :as str]
    [open-on-github.git :refer [get-git-info get-branch build-github-url]]))


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
                   (is false "timeout reached, test failed"))))))))


(deftest test-get-branch
  (testing "runs a subprocess and returns the branch name"
    (async done
           (let [t (timeout 100)
                 fake-editor :fake-editor
                 fake-run-process (fn [executable args]
                                    (is (= executable "git"))
                                    (is (= args ["rev-parse" "--abbrev-ref" "HEAD"]))
                                    (go {:status 0 :out ["cool-branch\n"]}))
                 r (get-branch fake-editor fake-run-process)]
             (go
               (let [[v ch] (alts! [t r])]
                 (if (= ch r)
                   (do
                     (is (= v "cool-branch"))
                     (done))
                   (is false "timeout reached, test failed"))))))))


(deftest test-build-github-url
  (testing "contains the branch name"
    (let [branch-name "main"
          git-info {:branch branch-name}
          result (build-github-url git-info)]
      (is (str/includes? result branch-name)))))
