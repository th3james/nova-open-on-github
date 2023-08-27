(ns open-on-github.nova-interface)


(defprotocol INova

  (register-command [_ command-id command-fn]))


(defrecord RealNova
  []

  INova

  (register-command
    [_ command-id command-fn]
    (println "Registering command" command-id)
    (js/nova.commands.register command-id command-fn)))


(defrecord FakeNova
  []

  INova

  (register-command
    [_ command-id _command-fn]
    (println "Registering command" command-id)))
