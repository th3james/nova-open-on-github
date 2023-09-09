(ns open-on-github.git-test
  (:require
    [cljs.core.async :refer [<! >! go]]
    [cljs.test :refer [async deftest is testing]]
    [clojure.string :as str]
    [open-on-github.git :refer [get-git-info get-branch build-github-url parse-url-from-origin]]
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
                     (is (= r (:status :ok)))
                     (is (= r (:branch "cool-branch")))
                     (>! finished-chan true))))))))

  (testing "when the subprocess fails, it returns an error"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor :fake-editor
                     fake-run-process (fn [_ _args]
                                        (go {:status 1 :out ["Oh dear!"]}))]
                 (go
                   (let [r (<! (get-branch fake-editor fake-run-process))]
                     (is (= r (:status :error)))
                     (is (= r (:branch "Oh dear!")))
                     (>! finished-chan true)))))))))


(deftest test-build-github-url
  (testing "contains the branch name"
    (let [branch-name "main"
          git-info {:branch branch-name}
          result (build-github-url git-info)]
      (is (str/includes? result branch-name))))

  (testing "is prefixed with the github path built from the origin url"
    (let [origin-url "git@github.com:cool-guy/nice-project.git"
          git-info {:origin-url origin-url}
          result (build-github-url git-info)]
      (is (str/starts-with? result (parse-url-from-origin origin-url))))))


(deftest test-parse-url-from-origin
  (testing "starts with github.com"
    (let [origin-url "git@github.com:cool-guy/nice-project.git"
          result (parse-url-from-origin origin-url)]
      (is (str/starts-with? result "https://github.com"))))

  (testing "contains the repo owner"
    (let [origin-url "git@github.com:cool-guy/nice-project.git"
          result (parse-url-from-origin origin-url)]
      (is (str/includes? result "cool-guy"))))

  (testing "ends with the repo name"
    (let [origin-url "git@github.com:cool-guy/nice-project.git"
          result (parse-url-from-origin origin-url)]
      (is (str/ends-with? result "nice-project/")))))
