(ns open-on-github.git
  (:require
    [cljs.core.async :refer [<! >! go go-loop chan]]
    [clojure.string :as str]
    [open-on-github.dependencies :refer [nova]]
    [open-on-github.nova-interface :refer [run-process]]
    [open-on-github.result :refer [ok error]]))


(defn get-branch
  ([editor] 
   (get-branch editor (fn [executable args cwd] (run-process @nova executable args cwd))))
  ([{:keys [document-parent-dir]} run-process-fn]
   (go
     (let [process-result (<! (run-process-fn "git" ["rev-parse" "--abbrev-ref" "HEAD"] document-parent-dir))]
       (if (= (:exit process-result) 0)
         (ok (str/trim-newline (apply str (:out process-result))))
         (error (str/trim-newline (apply str (:err process-result)))))))))


(defn get-origin
  ([editor] 
   (get-origin editor (fn [executable args cwd] (run-process @nova executable args cwd))))
  ([{:keys [document-parent-dir]} run-process-fn]
   (go (let [process-result (<! (run-process-fn "git" ["config" "--get" "remote.origin.url"] document-parent-dir))]
         (if (= (:exit process-result) 0)
           (ok (str/trim-newline (apply str (:out process-result))))
           (error (str/trim-newline (apply str (:err process-result)))))))))


(defn get-git-info
  ([editor] (get-git-info editor get-branch get-origin))
  ([editor get-branch-fn get-origin-fn]
   (let [process-chan (chan)
         result-chan (chan 1)
         commands [[:branch get-branch-fn]
                   [:origin-url get-origin-fn]]]
     (doseq [command commands]
       (let [val-name (first command)
             cmd-fn (second command)]
         (go (>! process-chan {:name val-name :result (<! (cmd-fn editor))}))))
     (go-loop [git-info {:status :ok}
               pending-results 2]
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
    origin-url :origin-url}]

  (str (parse-url-from-origin origin-url) "blob/" branch "/"))
