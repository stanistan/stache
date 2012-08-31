(ns stache.conf-test
  (:use stache.config clojure.test))

(deftest configs

  (testing "Default configurations"
    (is (= (:path-to config-default) path-to-templates))
    (is (= (:suffix config-default) template-suffix)))

  (config :path-to "resources" :suffix ".m")
  (testing "Changing config"
    (is (= path-to-templates "resources"))
    (is (= template-suffix ".m")))

  (reset-config)
  (testing "Rest to default configurations"
    (is (= (:path-to config-default) path-to-templates))
    (is (= (:suffix config-default) template-suffix))))
