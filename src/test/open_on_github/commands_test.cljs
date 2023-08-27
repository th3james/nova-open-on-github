(ns open-on-github.commands-test
  (:require
    [cljs.core.async :refer [>! chan go timeout alts!]]
    [cljs.test :refer [async deftest is testing]]
    [open-on-github.commands :refer [open-current-file]]
    [open-on-github.git :refer [build-github-url]]))


(deftest test-open-current-file
  (testing "gets git info, builds url and opens the browser"
    (async done
           (let [finished-chan (chan 1)
                 t (timeout 100)
                 fake-editor :fake-editor
                 fake-git-info {:branch "master"}
                 fake-get-git-info (fn [e]
                                     (is (= e fake-editor))
                                     fake-git-info)
                 fake-open-url (fn [url]
                                 (is (= url (build-github-url fake-git-info)))
                                 (go (>! finished-chan true)))]
             (open-current-file fake-editor fake-get-git-info fake-open-url)
             (go
               (let [[_ ch] (alts! [t finished-chan])]
                 (if (= ch finished-chan)
                   (done)
                   (is false "Timeout reached, test failed"))))))))
