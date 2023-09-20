(ns open-on-github.commands-test
  (:require
    [cljs.core.async :refer [>! go]]
    [cljs.test :refer [async deftest is testing]]
    [clojure.string :as str]
    [open-on-github.commands :refer [open-current-file]]
    [open-on-github.git :refer [build-github-url]]
    [open-on-github.nova-helpers :refer [parse-editor]]
    [open-on-github.test-helpers :refer [with-timeout]]))


(deftest test-open-current-file-success
  (testing "gets git info, builds url and opens the browser"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor #js {"document" #js {"path" "fake/path.tsx"}}
                     fake-git-info {:status :ok :branch "master"}
                     fake-get-git-info (fn [e]
                                         (is (= e (parse-editor fake-editor)))
                                         (go fake-git-info))
                     fake-open-url (fn [url]
                                     (is (= url (build-github-url fake-git-info)))
                                     (go (>! finished-chan true)))]
                 (open-current-file fake-editor fake-get-git-info fake-open-url (fn []))))))))


(deftest test-open-current-file-fail
  (testing "if git info fails, it logs an error"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor #js {"document" #js {"path" "fake/path.tsx"}}
                     fake-get-git-info (fn [_]
                                         (go {:status :error :errors {:branch "whoops"}}))
                     fake-open-url (fn [])
                     fake-log (fn [level message]
                                (is (= level :error))
                                (is (str/includes? message "whoops"))
                                (go (>! finished-chan true)))]
                 (open-current-file fake-editor fake-get-git-info fake-open-url fake-log)))))))
