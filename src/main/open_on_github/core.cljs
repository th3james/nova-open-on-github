(ns open-on-github.core
  (:require
    [open-on-github.commands :as commands]
    [open-on-github.dependencies :refer [nova]]
    [open-on-github.nova-interface :refer [RealNova register-command]]))


(defn main
  []
  (println "In ClojureScript main")
  (reset! nova (RealNova.))
  (register-command @nova "open-on-github.openCurrentFile" commands/open-current-file)
  (println "Finished ClojureScript main"))
