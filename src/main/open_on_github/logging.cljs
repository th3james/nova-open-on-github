(ns open-on-github.logging)


(defn log
  [level message]
  (println (str level ": " message)))
