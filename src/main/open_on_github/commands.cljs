(ns open-on-github.commands
  (:require
    [cljs.core.async :refer [<! go]]
    [open-on-github.dependencies :refer [nova]]
    [open-on-github.git :refer [get-git-info build-github-url]]
    [open-on-github.logging :refer [log]]
    [open-on-github.nova-helpers :refer [parse-editor]]
    [open-on-github.nova-interface :refer [open-url]]
    [open-on-github.result :refer [unwrap]]))


(defn open-current-file
  ([editor]
   (open-current-file editor get-git-info (fn [url] (open-url @nova url)) log))
  ([editor get-git-info-fn open-url-fn log-fn]
   (let [editor (parse-editor editor)]
     (go
       (let [git-info (<! (get-git-info-fn editor))]
         (if (= (:status git-info) :ok)
           (let [url-result (build-github-url git-info)]
             (if (= (:status url-result) :ok)
               (open-url-fn (unwrap url-result))
               (log-fn :error (str "Error building url: " url-result))))
           (log-fn :error (str "Error getting git info: " git-info))))))))
