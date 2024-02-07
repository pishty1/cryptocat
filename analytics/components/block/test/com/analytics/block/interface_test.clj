(ns com.analytics.block.interface-test
  (:require [clojure.test :as test :refer :all]
            [com.analytics.block.interface :as block]))

(deftest dummy-test
  (is (= 1 1)))


(deftest hello--when-called-with-a-name--then-return-hello-phrase
  (is (= "Hello Lisa!!"
         (block/hello "Lisa"))))

(deftest record-processing--when-called-with-a-topic-name--then-prints-received-message
  (is (= "Received message:  Hello World!"
         (block/start-c "test_topic"))))