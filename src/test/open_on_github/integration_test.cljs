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
      [_ executable args cwd]
      (let [expectations (:run-process expectations)]
        (if-let [ret-val (some (fn [e]
                                 (if (and (= executable (:executable e))
                                          (= args (:args e))
                                          (= cwd (:cwd e)))
                                   (:result e)
                                   nil))

                               expectations)]
          (go ret-val)
          (throw (js/Error.
                   (str "run-process called with unexpected arguments {:executable " executable " :args " args " :cwd " cwd "}"))))))

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
               (let [fake-editor #js {"document" #js {"path" "fake/path.tsx"}}
                     fake-get-branch {:executable "git"
                                      :args ["rev-parse" "--abbrev-ref" "HEAD"]
                                      :cwd "fake/"
                                      :result {:exit 0 :out ["cool-branch\n"]}}
                     fake-get-origin {:executable "git"
                                      :args ["config", "--get", "remote.origin.url"]
                                      :cwd "fake/"
                                      :result {:exit 0 :out ["git@github.com:cool-guy/nice-project.git\n"]}}
                     fake-nova (fake-nova-factory
                                 {:run-process [fake-get-branch fake-get-origin]
                                  :open-url {:url "https://github.com/cool-guy/nice-project/blob/cool-branch/"
                                             :finished-chan finished-chan}})]
                 (reset! nova fake-nova)
                 (open-current-file fake-editor)))))))
