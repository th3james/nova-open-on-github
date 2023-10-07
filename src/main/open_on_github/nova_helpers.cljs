(ns open-on-github.nova-helpers)


(defn get-parent-dir
  [file-path]
  (let [pattern #"^(.+)/[^/]+$"
        matches (.exec pattern file-path)]
    (if matches
      (aget matches 1)
      "No parent directory found")))


(defn parse-editor
  [editor]
  (let [document (.-document editor)
        path (.-path document)
        parent-path (get-parent-dir path)]
    {:document-path path
     :document-parent-dir parent-path}))

