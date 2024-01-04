(ns com.analytics.cli.core
  (:require [com.analytics.block.interface :as block])
  (:gen-class))

(defn -main
  [& args]
  (block/hello (first args))
  (System/exit 0))
