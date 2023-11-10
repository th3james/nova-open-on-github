(ns open-on-github.core
  (:require
    [open-on-github.commands :as commands]
    [open-on-github.dependencies :as dependencies]
    [open-on-github.nova-interface :as nova-interface]
    [open-on-github.logging :refer [log]]))


(defn setup-commands
  [nova-instance]
  (nova-interface/register-command nova-instance "open-on-github.openCurrentFile" commands/open-current-file))


(defn main
  []
  (log :debug "In ClojureScript main")
  (reset! dependencies/nova (nova-interface/RealNova.))
  (setup-commands @dependencies/nova)
  (log :debug "Finished ClojureScript main"))
