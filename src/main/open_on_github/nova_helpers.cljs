(ns open-on-github.nova-helpers
  (:require
    [open-on-github.path :refer [get-parent-dir]]))


(defn parse-editor
  [editor]
  (let [document (.-document editor)
        path (.-path document)
        parent-path (get-parent-dir path)]
    {:document-path path
     :document-parent-dir parent-path}))
