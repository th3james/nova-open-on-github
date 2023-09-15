(ns open-on-github.git
  (:require
    [cljs.core.async :refer [<! >! go go-loop chan]]
    [clojure.string :as str]
    [open-on-github.dependencies :refer [nova]]
    [open-on-github.nova-interface :refer [run-process]]))


(defn get-branch
  ([editor] (get-branch editor (fn [executable args] (run-process @nova executable args))))
  ([editor run-process-fn]
   (go
     (let [process-result (<! (run-process-fn "git" ["rev-parse" "--abbrev-ref" "HEAD"]))]
       (if (= (:exit process-result) 0)
         {:status :ok :branch (str/trim-newline (apply str (:out process-result)))}
         {:status :error :error (str/trim-newline (apply str (:err process-result)))})))))


(defn get-origin
  []
  (go {:status :error :error "not implemented"}))


(defn get-git-info
  ([editor] (get-git-info editor get-branch get-origin))
  ([editor get-branch-fn get-origin-fn]
   (let [process-chan (chan)
         result-chan (chan 1)
         commands [[:branch get-branch-fn]
                   [:origin-url get-origin-fn]]]
     (println "Calling git commands")
     (doseq [command commands]
       (let [k (first command)
             cmd-fn (second command)]
         (go (>! process-chan {:k k :result (<! (cmd-fn editor))}))))
     (go-loop [git-info {:status :ok}
               pending-results 2]
       (if (zero? pending-results)
         (do
           (println "\t\treturning git-info " git-info)
           (>! result-chan git-info))
         (let [result (<! process-chan)]
           (println "\tgit-info" git-info)
           (println "\tresult" result)
           (if (= (:status (:result result)) :error)
             (recur (assoc git-info
                           :status :error
                           :errors (assoc (:errors git-info)
                                          (:k result) (:error (:result result))))
                    (dec pending-results))
             (recur (assoc git-info
                           (:k result) (get (:result result) (:k result)))
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

  (str (parse-url-from-origin origin-url) branch "/"))
