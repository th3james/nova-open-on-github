(ns open-on-github.path)


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
      (aget matches 1)
      "No chroot found")))
