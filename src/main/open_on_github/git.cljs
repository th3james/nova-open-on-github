(ns open-on-github.git
  (:require
    [cljs.core.async :refer [<! go]]
    [clojure.string :as str]
    [open-on-github.dependencies :refer [nova]]
    [open-on-github.nova-interface :refer [run-process]]))


(defn get-branch
  ([editor] (get-branch editor (fn [executable args] (run-process @nova executable args))))
  ([editor run-process-fn]
   (go
     (let [process-result (<! (run-process-fn "git" ["rev-parse" "--abbrev-ref" "HEAD"]))]
       ;; TODO handle errors
       (print "process-result" process-result)
       (str/trim-newline (apply str (:out process-result)))))))


(defn get-git-info
  ([editor] (get-git-info editor get-branch))
  ([editor get-branch-fn]
   (go
     {:branch (<! (get-branch-fn editor))})))


(defn build-github-url
  [{branch :branch}]

  (str "github.com/" branch "/"))
