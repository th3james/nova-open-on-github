(ns open-on-github.git-test
  (:require
    [cljs.core.async :refer [<! >! go]]
    [cljs.test :refer [async deftest is testing]]
    [clojure.string :as str]
    [open-on-github.git :refer [get-git-info get-branch get-origin build-github-url parse-url-from-origin]]
    [open-on-github.result :refer [ok error]]
    [open-on-github.test-helpers :refer [with-timeout]]))


(deftest test-get-git-info-branch-success
  (testing "returns a map with the branch name"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor :fake-editor
                     fake-get-branch (fn [e]
                                       (is (= e fake-editor))
                                       (go (ok "cool-branch")))
                     fake-get-origin (fn [] (go (ok "nvm")))]
                 (go
                   (let [r (<! (get-git-info fake-editor fake-get-branch fake-get-origin))]
                     (is (= (:status r) :ok))
                     (is (= (:branch r) "cool-branch"))
                     (>! finished-chan :true)))))))))


(deftest test-get-git-info-origin-success
  (testing "returns a map with the origin url"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor :fake-editor
                     fake-get-branch (fn [_] (go (ok "cool-branch")))
                     fake-get-origin (fn []
                                       (go (ok "nvm")))]
                 (go
                   (let [r (<! (get-git-info fake-editor fake-get-branch fake-get-origin))]
                     (is (= (:status r) :ok))
                     (is (= (:origin-url r) "nvm"))
                     (>! finished-chan :true)))))))))


(deftest test-get-git-info-fail
  (testing "returns status error if anything fails"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor :fake-editor
                     fake-get-branch (fn [_]
                                       (go (error "nope")))
                     fake-get-origin (fn []
                                       (go (ok "origin")))]
                 (go
                   (let [r (<! (get-git-info fake-editor fake-get-branch fake-get-origin))]
                     (is (= (:status r) :error))
                     (is (= (:errors r) {:branch "nope"}))
                     (>! finished-chan :true)))))))))


(deftest test-get-branch-success
  (testing "runs a subprocess and returns the branch name"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor :fake-editor
                     fake-run-process (fn [executable args]
                                        (is (= executable "git"))
                                        (is (= args ["rev-parse" "--abbrev-ref" "HEAD"]))
                                        (go {:exit 0 :out ["cool-branch\n"]}))]
                 (go
                   (let [r (<! (get-branch fake-editor fake-run-process))]
                     (is (= (:status r) :ok))
                     (is (= (:val r) "cool-branch"))
                     (>! finished-chan true)))))))))


(deftest test-get-branch-fail
  (testing "when the subprocess fails, it returns an error"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor :fake-editor
                     fake-run-process (fn [_ _args]
                                        (go {:exit 1 :out [] :err ["Oh dear!"]}))]
                 (go
                   (let [r (<! (get-branch fake-editor fake-run-process))]
                     (is (= (:status r) :error))
                     (is (= (:error r) "Oh dear!"))
                     (>! finished-chan true)))))))))


(deftest test-get-origin-success
  (testing "runs a subprocess and returns the origin url"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-run-process (fn [executable args]
                                        (is (= executable "git"))
                                        (is (= args ["config" "--get" "remote.origin.url"]))
                                        (go {:exit 0 :out ["some-url\n"]}))]
                 (go
                   (let [r (<! (get-origin nil fake-run-process))]
                     (is (= (:status r) :ok))
                     (is (= (:val r) "some-url"))
                     (>! finished-chan true)))))))))


(deftest test-get-origin-fail
  (testing "when the subprocess fails, it returns an error"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-run-process (fn [_ _args]
                                        (go {:exit 1 :out [] :err ["Oh dear!\n"]}))]
                 (go
                   (let [r (<! (get-origin nil fake-run-process))]
                     (is (= (:status r) :error))
                     (is (= (:error r) "Oh dear!"))
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
      (is (str/starts-with? result (parse-url-from-origin origin-url)))))
  
  (testing "is a well formed github URL"
    (let [origin-url "git@github.com:cool-guy/nice-project.git"
          git-info {:origin-url origin-url :branch "main"}
          result (build-github-url git-info)]
      (is (re-matches #".*/blob/[^/]+/" result)))))


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
