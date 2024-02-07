(ns com.analytics.block.interface
  (:require [com.analytics.block.core :as core]))

(defn hello [name]
  (core/hello name))


(defn start-c [topic-name]
  (core/start-c topic-name))