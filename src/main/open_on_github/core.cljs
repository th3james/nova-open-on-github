(ns open-on-github.core
  (:require
    [open-on-github.commands :as commands]
    [open-on-github.dependencies :as dependencies]
    [open-on-github.nova-interface :as nova-interface]))


(defn setup-commands
  [nova-instance]
  (nova-interface/register-command nova-instance "open-on-github.openCurrentFile" commands/open-current-file))


(defn main
  []
  (println "In ClojureScript main")
  (reset! dependencies/nova (nova-interface/RealNova.))
  (setup-commands @dependencies/nova)
  (println "Finished ClojureScript main"))
