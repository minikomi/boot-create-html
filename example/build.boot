(set-env!
 :resource-paths #{"resources"}
 :source-paths #{"src"}
 :dependencies '[[poyo.co/boot-create-html "0.1.0-SNAPSHOT"]
                 [hiccup "1.0.5"]
                 [pandeiro/boot-http "0.8.3"]
                 [samestep/boot-refresh "0.1.0"]
                 ])

(require
 '[poyo.co.boot-create-html :refer [create-html]]
 '[pandeiro.boot-http :refer [serve]]
 '[samestep.boot-refresh :refer [refresh]]
 )

(deftask build-and-serve []
  (comp (watch)
        (refresh)
        (create-html)
        (serve :dir "target/public")
        (target)))
