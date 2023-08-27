(ns open-on-github.git
  (:require
    [cljs.core.async :refer [<! go]]))


(defn get-git-info
  [editor get-branch-fn]
  (go
    {:branch (<! (get-branch-fn editor))}))


(defn build-github-url
  [git-info]

  (str "Called build-github-url with " git-info))
