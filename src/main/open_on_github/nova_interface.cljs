(ns open-on-github.nova-interface
  (:require
    [cljs.core.async :refer [>! go]]))


(defprotocol INova

  (register-command [_ command-id command-fn])

  (run-process [_ editor executable args])

  (open-url [_ url]))


(defrecord RealNova
  []

  INova

  (register-command
    [_ command-id command-fn]
    (println "Registering command" command-id)
    (js/nova.commands.register command-id command-fn))


  (run-process
    [_ executable args opts]
    (go
      (println "RealNova running process not implemented")))


  (open-url
    [_ url]
    (go (println "RealNova open url not implemented" url))))


(defrecord StubNova
  []

  INova

  (register-command
    [_ command-id _command-fn]
    (println "StubNova register command " command-id))


  (run-process
    [_ executable args opts]
    (go
      (println "StubNova running process " executable args opts)
      {:out "fake output"}))


  (open-url
    [_ url]
    (println "Opening url" url)
    (go nil)))
