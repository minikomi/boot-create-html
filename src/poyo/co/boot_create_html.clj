(ns poyo.co.boot-create-html
  "Example tasks showing various approaches."
  {:boot/export-tasks true}
  (:require [boot.core :as boot :refer [deftask]]
            [boot.util :as util]
            [boot.pod  :as pod]
            [clojure.string :as s]
            [clojure.java.io :as io]))

(def processed-ns (atom #{}))
(def processed-data (atom {}))

(defn -init-ns-tracker [ns-pod]
  (let [src-paths (vec (boot/get-env :source-paths))]
    (pod/with-eval-in ns-pod
      (require 'ns-tracker.core)
      (def cns (ns-tracker.core/ns-tracker ~src-paths)))))

(defn ns-tracker-pod []
  (->> '[[ns-tracker "0.3.0"] [org.clojure/tools.namespace "0.2.11"]]
       (assoc (boot/get-env) :dependencies)
       pod/make-pod))

(defn -file-modified? [f]
  (when (< (get @processed-data (.getPath f)) (.lastModified f))
    (swap! processed-data assoc (.getPath f) (.lastModified f))
    true))

(defn -symbol-did-change? [cns-result ns-sym]
  (some #{ns-sym} cns-result))

(deftask create-html
  "Create pages in place of .html.edn files."
  [d data-ext EXT str "The extension used for data files. defaults to .html.edn."]
  (let [out-dir            (boot/tmp-dir!)
        ns-pod             (ns-tracker-pod)
        ext                (or data-ext ".html.edn")
        ext-regex          (re-pattern (str ext "$"))]
    ;; set up namespace tracking
    (-init-ns-tracker ns-pod)
    ;; begin task
    (boot/with-pre-wrap fs
      (let [temp-files (boot/by-ext [ext] (boot/input-files fs))
            changed-namespaces (pod/with-eval-in ns-pod (cns))]
        (doseq [temp-file temp-files
                :let      [f             (boot/tmp-file temp-file)
                           data          (read-string (slurp f))
                           data-path     (boot/tmp-path temp-file)
                           template-fn   (:template data)
                           ns-sym        (symbol (namespace template-fn))
                           initial?      (not (get @processed-ns [ns-sym data-path]))
                           should-build? (or initial?
                                             (-symbol-did-change? changed-namespaces ns-sym)
                                             (-file-modified? f))]]
          ;; initialize data on first run
          (when initial?
            (swap! processed-ns conj [ns-sym data-path])
            (swap! processed-data assoc (.getPath f) (.lastModified f)))
          (when should-build?
            (require ns-sym :reload)
            (if-let [template (ns-resolve ns-sym template-fn)]
              (let [out-path (s/replace data-path ext-regex ".html")
                    out-f    (io/file out-dir out-path)]
                (util/info "[HTML] Building [%s] using [%s].\n" out-path template-fn)
                (io/make-parents out-f)
                (spit out-f (template data)))
              (throw (Exception.
                      (format "Template function not found. [%s]" template-fn))))))
        ;; commit
        (-> fs (boot/add-resource out-dir) boot/commit!)))))
