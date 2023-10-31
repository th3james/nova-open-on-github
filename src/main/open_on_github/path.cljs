(ns open-on-github.path
  (:require
    [open-on-github.result :refer [ok error]]))


(defn get-parent-dir
  [file-path]
  (let [pattern #"^(.+)/[^/]+$"
        matches (.exec pattern file-path)]
    (if matches
      (aget matches 1)
      "No parent directory found")))


(defn chroot
  [file-path chroot-path]
  (let [pattern (js/RegExp. (str "^" chroot-path "/(.*)$"))
        matches (.exec pattern file-path)]
    (if matches
      (ok (aget matches 1))
      (error (str "Cannot chroot '" file-path "' to '" chroot-path "'")))))
