(ns open-on-github.nova-interface
  (:require
    [cljs.core.async :refer [>! chan go go-loop <! close!]]))


(defprotocol INova

  (register-command [_ command-id command-fn])

  (run-process [_ editor executable])

  (open-url [_ url]))


(defrecord RealNova
  []

  INova

  (register-command
    [_ command-id command-fn]
    (println "Registering command" command-id)
    (js/nova.commands.register command-id command-fn))


  (run-process
    [_ executable args]
    (let [msg-chan (chan)
          result-chan (chan 1)
          process (js/Process. "/usr/bin/env"  (clj->js {"args" (into-array (cons executable args))
                                                         "shell" true}))]

      (.onStdout process (fn [line] (go (>! msg-chan [:out line]))))
      (.onStderr process (fn [line] (go (>! msg-chan [:err line]))))
      (.onDidExit process (fn [status] (go (>! msg-chan [:exit status]))))

      (.start process)

      (go-loop [data {:out [] :err []}]
        (let [[type msg] (<! msg-chan)]
          (cond
            (= type :out) (recur (update data :out conj msg))
            (= type :err) (recur (update data :err conj msg))
            (= type :exit) (do (>! result-chan (assoc data :exit msg))
                               (close! msg-chan)))))
      result-chan))


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
    [_ executable args]
    (go
      (println "StubNova running process " executable args)
      {:out "fake output"}))


  (open-url
    [_ url]
    (println "Opening url" url)
    (go nil)))
