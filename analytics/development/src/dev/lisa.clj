(ns dev.lisa
  (:require [com.analytics.block.interface :as block]
            ))

(+ 1 2 3)


(comment
  (block/start-c "test_topic")
  (block/hello "Lisa")
  )
