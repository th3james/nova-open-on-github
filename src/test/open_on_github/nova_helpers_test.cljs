(ns open-on-github.nova-helpers-test
  (:require
    [cljs.test :refer [deftest is testing]]
    [open-on-github.nova-helpers :refer [parse-editor]]))


(deftest test-parse-editor
  (testing "returns a map with the parent directory of the document path"
    (let [fake-editor #js {"document" #js {"path" "fake/path.tsx"}}]
      (is (= (parse-editor fake-editor) {:document-parent-dir "fake/path.tsx"})))))
