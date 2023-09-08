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
       (str/trim-newline (apply str (:out process-result)))))))


(defn get-git-info
  ([editor] (get-git-info editor get-branch))
  ([editor get-branch-fn]
   (go
     {:branch (<! (get-branch-fn editor))})))


(defn parse-url-from-origin
  [origin-url]
  (let [split-origin (str/split origin-url #"://")]
    (str "https://github.com/")))


(defn build-github-url
  [{branch :branch
    origin-url :origin-url}]

  (str (parse-url-from-origin origin-url) branch "/"))
