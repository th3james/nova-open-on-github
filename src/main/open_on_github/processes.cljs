(ns open-on-github.processes
  (:require
    [cljs.core.async :refer [<! go]]
    [clojure.string :as str]
    [open-on-github.dependencies :refer [nova]]
    [open-on-github.nova-interface :as nova-interface]
    [open-on-github.result :refer [ok error]]))


(defn -prod-run-process
  [executable args cwd]
  (nova-interface/run-process @nova executable args cwd))


(defn run-process
  ([executable args cwd]
   (run-process executable args cwd -prod-run-process))
  ([executable args cwd run-process-fn]
   (go
     (let [result (<! (run-process-fn executable args cwd))]
       (if (= (:exit result) 0)
         (ok (str/trim-newline (apply str (:out result))))
         (error (str/trim-newline (apply str (:err result)))))))))
