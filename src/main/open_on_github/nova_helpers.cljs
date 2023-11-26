(ns open-on-github.nova-helpers
  (:require
    [open-on-github.path :refer [get-parent-dir]]))


(defn parse-editor
  [^js/Nova.TextEditor editor]
  (let [document (.-document editor)
        path (.-path document)
        parent-path (get-parent-dir path)
        selected-range (.-selectedRange editor)
        selected-lines (.getLineRangeForRange document selected-range)]
    (println "editor: " editor)
    (println "select-range " selected-range)
    (println "selected-lines " selected-lines)
    {:document-path path
     :document-parent-dir parent-path}))
