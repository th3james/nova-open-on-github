(ns open-on-github.integration-test
  (:require
    [cljs.core.async :refer [>! chan go timeout alts!]]
    [cljs.test :refer [async deftest is testing]]
    [open-on-github.commands :refer [open-current-file]]
    [open-on-github.dependencies :refer [nova]]
    [open-on-github.nova-interface :refer [INova]]))


(deftest test-integration-open-current-file
  (testing "gets git info, builds url and opens the browser"
    (async done
           (let [finished-chan (chan 1)
                 t (timeout 100)
                 fake-editor #js {"path" "fake/path"}
                 fake-nova (reify INova

                             (run-process
                               [_ executable args opts]
                               (go
                                 (is (= executable "git"))
                                 (is (= args ["rev-parse" "--abbrev-ref" "HEAD"]))
                                 (is (= opts {:cwd "fake/path"}))
                                 {:status 0 :out "master\n"}))

                             (open-url
                               [_ url]
                               (is (= url "master"))
                               (go nil)))]
             (reset! nova fake-nova)
             (open-current-file fake-editor)
             (go
               (let [[_ ch] (alts! [t finished-chan])]
                 (if (= ch finished-chan)
                   (done)
                   (is false "Timeout reached, test failed"))))))))
