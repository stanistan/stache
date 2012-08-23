(ns stache.core-test
  (:use clojure.test
        stache.core))

; (def temps
;   {:simple "Hi, {{name}}."
;    :ps "Oh You. {{>simple}}"})

; (deftest mustache-works
;   (testing "Initial mustache test."
;     (is (=
;       (render-template (:simple temps) {:name "Stan"})
;       "Hi, Stan."))
;     (is (=
;       (render-template (:ps temps) {:name "Stan"} :partials temps)
;       "Oh You. Hi, Stan."))))

; (deftest configs

;   (testing "Default configurations"
;     (is (= (:path-to config-default) path-to))
;     (is (= (:suffix config-default) suffix)))

;   (config :path-to "resources" :suffix ".m")
;   (testing "Changing config"
;     (is (= path-to "resources"))
;     (is (= suffix ".m")))

;   (reset-config)
;   (testing "Rest to default configurations"
;     (is (= (:path-to config-default) path-to))
;     (is (= (:suffix config-default) suffix))))

(deftest resources

  ; (is (=
  ;   (render-template "simple" {:name "Stan"})
  ;   "Hi, Stan.\n")) ; empty lines at the end of files

  ; (is (=
  ;   (render-template "ps" {:name "Stan"})
  ;     "Oh You. Hi, Stan.\n\n"))) ; empty lines at the end of files

  (is (=
    (render-template "i/t" {})
    "All good in the hood.\n\n")))
