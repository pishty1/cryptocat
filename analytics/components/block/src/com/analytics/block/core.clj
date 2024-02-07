(ns com.analytics.block.core
  (:require [jackdaw.client :as jc]
            [jackdaw.serdes :refer [string-serde edn-serde]]
            [jackdaw.serdes.json :refer [serde]]))
  
; hi
(defn hello [name]
  (str "Hello " name "!!"))


(defn poll-and-loop!
  "Continuously fetches records every `poll-ms`, processes them with `processing-fn` and commits offset after each poll."
  [consumer processing-fn continue?]
  (let [poll-ms 5000]
    (loop []
      (when @continue?
        (let [records (jc/poll consumer poll-ms)]
          (when (seq records)
            (processing-fn records)
            (.commitSync consumer))
          (recur))))))

(defn process-messages! [topic-name processing-fn]
  (println "Starting consumer with topic: " topic-name " ...")
  (let [continue? (atom true)]
    (with-open [consumer (jc/subscribed-consumer {"bootstrap.servers" "localhost:9092"
                                                  "group.id" "groupid"}
                                                 [{:topic-name topic-name 
                                                   :key-serde (string-serde)
                                                   :value-serde (serde)}])]
      (poll-and-loop! consumer processing-fn continue?))))

(defn start-c [topic-name]
  (process-messages! topic-name 
                     (fn [records]
                       (doseq [record records]
                         (println "Received message: " (:value record))))))

(comment
  (start-c "test_topic")
  (start-c "test_topic_2")
  )