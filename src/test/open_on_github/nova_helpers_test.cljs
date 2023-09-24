(ns open-on-github.nova-helpers-test
  (:require
    [cljs.test :refer [deftest is testing]]
    [open-on-github.nova-helpers :refer [parse-editor]]))


(deftest test-parse-editor
  (testing "returns a map with the parent directory of the document path"
    (let [fake-editor #js {"document" #js {"path" "hat/fake/path.tsx"}}]
      (is (= (:document-parent-dir (parse-editor fake-editor)) "hat/fake")))))
