(ns open-on-github.processes-test
  (:require
    [cljs.core.async :refer [<! >! go]]
    [cljs.test :refer [async deftest is testing]]
    [open-on-github.processes :refer [run-process]]
    [open-on-github.test-helpers :refer [with-timeout]]))


(deftest test-run-process-success
  (testing "runs a subprocess and when exit: 0 returns ok with the trimmed output"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [work-dir "some/path"
                     fake-nova-run-process (fn [executable args cwd]
                                             (is (= executable "ls"))
                                             (is (= args ["a" "--b"]))
                                             (is (= cwd work-dir))
                                             (go {:exit 0 :out ["cool-file\n"]}))]
                 (go
                   (let [r (<! (run-process "ls" ["a" "--b"] work-dir fake-nova-run-process))]
                     (is (= (:status r) :ok))
                     (is (= (:val r) "cool-file"))
                     (>! finished-chan true)))))))))

(deftest test-run-process-error
  (testing "runs a subprocess and when exit: >0 returns error with the trimmed stderr"
    (async done
           (with-timeout done
             (fn [finished-chan]
               (let [fake-nova-run-process (fn [_executable _args _cwd]
                                             (go {:exit 1 :err ["uh\n", "oh\n"]}))]
                 (go
                   (let [r (<! (run-process "doh" [] nil fake-nova-run-process))]
                     (is (= (:status r) :error))
                     (is (= (:error r) "uh\noh"))
                     (>! finished-chan true)))))))))
