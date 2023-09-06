(ns open-on-github.integration-test
  (:require
    [cljs.core.async :refer [>! go]]
    [cljs.test :refer [async deftest is testing]]
    [open-on-github.commands :refer [open-current-file]]
    [open-on-github.dependencies :refer [nova]]
    [open-on-github.nova-interface :refer [INova]]
    [open-on-github.test-helpers :refer [with-timeout]]))


(deftest test-integration-open-current-file
  (testing "gets git info, builds url and opens the browser"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor #js {"document" #js {"path" "fake/path"}}
                     fake-nova (reify INova
                                 (run-process
                                   [_ executable args]
                                   (is (= executable "git"))
                                   (is (= args ["rev-parse" "--abbrev-ref" "HEAD"]))
                                   (go {:status 0 :out ["master\n"]}))

                                 (open-url
                                   [_ url]
                                   (is (= url "github.com/master/"))
                                   (go (>! finished-chan true))))]
                 (reset! nova fake-nova)
                 (open-current-file fake-editor)))))))
