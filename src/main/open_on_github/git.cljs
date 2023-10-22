(ns open-on-github.git
  (:require
    [cljs.core.async :refer [<! >! go go-loop chan]]
    [clojure.string :as str]
    [open-on-github.path :refer [chroot]]
    [open-on-github.processes :refer [run-process]]))


(defn get-branch
  ([editor] (get-branch editor run-process))
  ([{:keys [document-parent-dir]} run-process-fn]
   (run-process-fn "git" ["rev-parse" "--abbrev-ref" "HEAD"] document-parent-dir)))


(defn get-origin
  ([editor] (get-origin editor run-process))
  ([{:keys [document-parent-dir]} run-process-fn]
   (run-process-fn "git" ["config" "--get" "remote.origin.url"] document-parent-dir)))

(defn get-root
  ([editor] (get-root editor run-process))
  ([{:keys [document-parent-dir]} run-process-fn]
   (run-process-fn "git" ["rev-parse" "--show-toplevel"] document-parent-dir)))


(defn get-git-info
  ([editor] (get-git-info editor get-branch get-origin get-root))
  ([editor get-branch-fn get-origin-fn get-root-fn]
   (let [process-chan (chan)
         result-chan (chan 1)
         commands [[:branch get-branch-fn]
                   [:origin-url get-origin-fn]
                   [:git-root get-root-fn]]]
     (doseq [command commands]
       (let [val-name (first command)
             cmd-fn (second command)]
         (go (>! process-chan {:name val-name :result (<! (cmd-fn editor))}))))
     (go-loop [git-info {:status :ok
                         :editor editor}
               pending-results (count commands)]
       (if (zero? pending-results)
         (>! result-chan git-info)
         (let [cmd-result (<! process-chan)]
           (if (= (:status (:result cmd-result)) :error)
             (recur (assoc git-info
                           :status :error
                           :errors (assoc (:errors git-info)
                                          (:name cmd-result) (:error (:result cmd-result))))
                    (dec pending-results))
             (recur (assoc git-info
                           (:name cmd-result) (:val (:result cmd-result)))
                    (dec pending-results))))))
     result-chan)))


(defn parse-url-from-origin
  [origin-url]
  (let [split-origin (str/split origin-url #":(.*)/(.*).git")
        [_ owner repo-name] split-origin]
    (str "https://github.com/" owner "/" repo-name "/")))


(defn build-github-url
  [{branch :branch
    origin-url :origin-url
    git-root :git-root
    editor :editor}]

  (let [doc-path (chroot (:document-path editor) git-root) ]
    (str 
      (parse-url-from-origin origin-url) 
      "blob/" 
      branch "/"
      doc-path)))
