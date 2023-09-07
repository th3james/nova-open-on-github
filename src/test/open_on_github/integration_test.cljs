(ns open-on-github.integration-test
  (:require
    [cljs.core.async :refer [>! go]]
    [cljs.test :refer [async deftest is testing]]
    [open-on-github.commands :refer [open-current-file]]
    [open-on-github.dependencies :refer [nova]]
    [open-on-github.nova-interface :refer [INova]]
    [open-on-github.test-helpers :refer [with-timeout]]))


(defn fake-nova-factory
  [expectations]
  (reify INova
    (run-process
      [_ executable args]
      (let [expectation (:run-process expectations)]
        (is (= executable (:executable expectation)))
        (is (= args (:args expectation)))
        (go (:result expectation))))

    (open-url
      [_ url]
      (let [expectation (:open-url expectations)]
        (is (= url (:url expectation)))
        (go (>! (:finished-chan expectation) true))))))


(deftest test-integration-open-current-file
  (testing "gets git info, builds url and opens the browser"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-editor #js {"document" #js {"path" "fake/path"}}
                     fake-nova (fake-nova-factory
                                 {:run-process {:executable "git"
                                                :args ["rev-parse" "--abbrev-ref" "HEAD"]
                                                :result {:status 0 :out ["master\n"]}}
                                  :open-url {:url "github.com/master/"
                                             :finished-chan finished-chan}})]
                 (reset! nova fake-nova)
                 (open-current-file fake-editor)))))))
