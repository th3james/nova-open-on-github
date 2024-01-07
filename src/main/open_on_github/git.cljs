(ns open-on-github.git
  (:require
    [cljs.core.async :refer [<! >! go go-loop chan]]
    [clojure.string :as str]
    [open-on-github.path :refer [chroot]]
    [open-on-github.processes :refer [run-process]]
    [open-on-github.result :refer [ok unwrap]]))


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


(defn extract-path
  [origin-url]
  (some-> origin-url
          (str/replace #"^https://(.*\@)?github.com/" "")
          (str/replace #"^ssh://git@github.com/" "")
          (str/replace #"^git@github.com:" "")))


(defn github-origin-from-path
  [[_ owner repo]]
  (str "https://github.com/" owner "/" repo "/"))


(defn parse-url-from-origin
  [origin-url]
  (some->> origin-url
           (extract-path)
           (re-matches #"(.*)/(.*)\.git")
           (github-origin-from-path)))


(defn build-github-url
  [{branch :branch
    origin-url :origin-url
    git-root :git-root
    editor :editor}]

  (let [doc-path-result (chroot (:document-path editor) git-root)]
    (if (= (:status doc-path-result) :error)
      doc-path-result
      (let [doc-path (unwrap doc-path-result)]
        (ok (str
              (parse-url-from-origin origin-url)
              "blob/"
              branch "/"
              doc-path))))))
