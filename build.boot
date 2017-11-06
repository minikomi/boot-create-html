(def project 'poyo.co/boot-create-html)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"src"}
          :dependencies   '[[org.clojure/clojure "RELEASE"]
                            [boot/core "RELEASE" :scope "test"]
                            [adzerk/boot-test "RELEASE" :scope "test"]])

(task-options!
 pom {:project     project
      :version     version
      :description "Create html pages from .html.edn"
      :url         "http://example/FIXME"
      :scm         {:url "https://github.com/yourname/boot-create-html"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask build
  "Build and install the project locally."
  []
  (comp (pom) (jar) (install)))

(require '[adzerk.boot-test :refer [test]]
         '[boot-create-html.core :refer [create-html]])
