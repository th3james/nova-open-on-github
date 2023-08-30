(ns open-on-github.commands
  (:require
    [cljs.core.async :refer [go]]
    [open-on-github.dependencies :refer [nova]]
    [open-on-github.git :refer [get-git-info build-github-url]]
    [open-on-github.nova-interface :refer [open-url]]))


(defn open-current-file
  ([editor]
   (open-current-file editor get-git-info (fn [url] (open-url @nova url))))
  ([editor get-git-info-fn open-url-fn]
   (go
     (let [git-info (get-git-info-fn editor)
           url (build-github-url git-info)]
       (open-url-fn url)))))
