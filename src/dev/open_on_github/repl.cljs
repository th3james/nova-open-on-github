(ns open-on-github.repl
  (:require
    [open-on-github.dependencies :refer [nova]]
    [open-on-github.nova-interface :refer [FakeNova]]))


(defn main
  []
  ; Note: this function doesn't appear to actually do anything
  (println "In ClojureScript main")
  (reset! nova (FakeNova.)))


(print "In ClojureScript REPL" nova)
