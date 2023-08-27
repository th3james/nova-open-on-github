(ns open-on-github.commands
  (:require
    [cljs.core.async :refer [go]]
    [open-on-github.git :refer [build-github-url]]))



(defn open-current-file
  ([editor]
   (open-current-file editor nil nil))
  ([editor get-git-info-fn open-url-fn]
   (go
     (println "Open on GitHub command called"))))
