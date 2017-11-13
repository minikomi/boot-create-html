(set-env! :resource-paths #{"src"}
          :dependencies   '[[org.clojure/clojure "RELEASE"]
                            [boot/core "RELEASE" :scope "test"]
                            [adzerk/boot-test "RELEASE" :scope "test"]
                            [adzerk/bootlaces "0.1.13"]
                            ])

(require
 '[poyo.co.boot-create-html :refer [create-html]]
 '[adzerk.bootlaces :refer [bootlaces! build-jar push-snapshot push-release]])

(def project 'poyo.co/boot-create-html)
(def +version+ "0.1.0-SNAPSHOT")

(bootlaces! +version+)

(task-options!
 pom {:project     project
      :version     +version+
      :description "Create html pages from .html.edn"
      :url         "https://github.com/minikomi/boot-create-html"
      :scm         {:url "https://github.com/minikomi/boot-create-html"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}})
