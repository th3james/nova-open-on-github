(ns open-on-github.nova-helpers)


(defn parse-editor
  [editor]
  (let [document (.-document editor)]
    {:path (.-path document)}))
