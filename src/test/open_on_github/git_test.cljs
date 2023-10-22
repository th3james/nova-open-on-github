(ns open-on-github.git-test
  (:require
    [cljs.core.async :refer [<! >! go]]
    [cljs.test :refer [async deftest is testing]]
    [clojure.string :as str]
    [open-on-github.git :refer [get-git-info get-branch get-origin get-root build-github-url parse-url-from-origin]]
    [open-on-github.path :refer [chroot]]
    [open-on-github.result :refer [ok error]]
    [open-on-github.test-helpers :refer [with-timeout]]))


(deftest test-get-git-info-editor
  (testing "returns a map with given editor"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor :fake-editor
                     fake-getter (fn [] (go (ok "nvm")))]
                 (go
                   (let [r (<! (get-git-info fake-editor fake-getter fake-getter fake-getter))]
                     (is (= (:status r) :ok))
                     (is (= (:editor r) fake-editor))
                     (>! finished-chan :true)))))))))


(deftest test-get-git-info-branch-success
  (testing "returns a map with the branch name"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor :fake-editor
                     fake-getter (fn [] (go (ok "nvm")))
                     fake-get-branch (fn [e]
                                       (is (= e fake-editor))
                                       (go (ok "cool-branch")))]
                 (go
                   (let [r (<! (get-git-info fake-editor fake-get-branch fake-getter fake-getter))]
                     (is (= (:status r) :ok))
                     (is (= (:branch r) "cool-branch"))
                     (>! finished-chan :true)))))))))


(deftest test-get-git-info-origin-success
  (testing "returns a map with the origin url"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor :fake-editor
                     fake-getter (fn [] (go (ok "nvm")))
                     fake-get-origin (fn []
                                       (go (ok "dat-origin")))]
                 (go
                   (let [r (<! (get-git-info fake-editor fake-getter fake-get-origin fake-getter))]
                     (is (= (:status r) :ok))
                     (is (= (:origin-url r) "dat-origin"))
                     (>! finished-chan :true)))))))))


(deftest test-get-git-info-git-root-success
  (testing "returns a map with the git root"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor :fake-editor
                     fake-getter (fn [] (go (ok "nvm")))
                     fake-get-root (fn [e]
                                     (is (= e fake-editor))
                                     (go (ok "/the/root")))]
                 (go
                   (let [r (<! (get-git-info fake-editor fake-getter fake-getter fake-get-root))]
                     (is (= (:status r) :ok))
                     (is (= (:git-root r) "/the/root"))
                     (>! finished-chan :true)))))))))


(deftest test-get-git-info-fail
  (testing "returns status error if anything fails"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor :fake-editor
                     fake-getter-fail (fn [_]
                                        (go (error "nope")))
                     fake-getter-success (fn []
                                           (go (ok "origin")))]
                 (go
                   (let [r (<! (get-git-info fake-editor fake-getter-fail fake-getter-success fake-getter-success))]
                     (is (= (:status r) :error))
                     (is (= (:errors r) {:branch "nope"}))
                     (>! finished-chan :true)))))))))


(deftest test-get-branch-success
  (testing "runs a subprocess and returns the branch name"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor {:document-parent-dir "some/path"}
                     fake-run-process (fn [executable args cwd]
                                        (is (= executable "git"))
                                        (is (= args ["rev-parse" "--abbrev-ref" "HEAD"]))
                                        (is (= cwd "some/path"))
                                        (go (ok "cool-branch")))]
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
                                        (go (error "Oh dear!")))]
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
               (let [fake-editor {:document-parent-dir "dat/path"}
                     fake-run-process (fn [executable args cwd]
                                        (is (= executable "git"))
                                        (is (= args ["config" "--get" "remote.origin.url"]))
                                        (is (= cwd "dat/path"))
                                        (go (ok "some-url")))]
                 (go
                   (let [r (<! (get-origin fake-editor fake-run-process))]
                     (is (= (:status r) :ok))
                     (is (= (:val r) "some-url"))
                     (>! finished-chan true)))))))))


(deftest test-get-origin-fail
  (testing "when the subprocess fails, it returns an error"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-run-process (fn [_ _args]
                                        (go (error "Oh dear!")))]
                 (go
                   (let [r (<! (get-origin nil fake-run-process))]
                     (is (= (:status r) :error))
                     (is (= (:error r) "Oh dear!"))
                     (>! finished-chan true)))))))))


(deftest test-get-root-success
  (testing "runs git rev-parse --show-toplevel and returns the output"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor {:document-parent-dir "/src/repo/subdir"}
                     fake-run-process (fn [executable args cwd]
                                        (is (= executable "git"))
                                        (is (= args ["rev-parse" "--show-toplevel"]))
                                        (is (= cwd "/src/repo/subdir"))
                                        (go (ok "/src/repo/")))]
                 (go
                   (let [r (<! (get-root fake-editor fake-run-process))]
                     (is (= (:status r) :ok))
                     (is (= (:val r) "/src/repo/"))
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

  (testing "is suffixed with the document path chrooted to the git root"
    (let [git-info {:editor {:document-path "/User/some/path.cljs"}
                    :git-root "/User/some"}
          result (build-github-url git-info)]
      (is (str/ends-with? result (chroot "/User/some/path.cljs" "/User/some")))
      (is (not (str/ends-with? result "/User/some/path.cljs")))))

  (testing "is a well formed github URL"
    (let [origin-url "git@github.com:cool-guy/nice-project.git"
          git-info {:origin-url origin-url
                    :branch "main"
                    :git-root "/root"
                    :editor {:document-path "/root/veg.tzt"}}
          result (build-github-url git-info)]
      (is (re-matches #".*/blob/.*" result)))))


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
