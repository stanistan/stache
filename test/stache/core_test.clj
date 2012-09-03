(ns stache.core-test
  (:use clojure.test
        stache.core))

(def temps
  {:simple "Hi, {{name}}."
   :ps "Oh You. {{>simple}}"})

(deftest mustache-works
  (testing "Initial mustache test."
    (is (=
      (render-template (:simple temps) {:name "Stan"})
      "Hi, Stan."))
    (is (=
      (render-template (:ps temps) {:name "Stan"} :partials temps)
      "Oh You. Hi, Stan."))))

(deftest resources

  ; All of these are in test/resources

  (testing "looking for resources"
    (is (=
      (render-template "simple" {:name "Stan"})
      "Hi, Stan.\n")) ; empty lines at the end of files

    (is (=
      (render-template "ps" {:name "Stan"})
        "Oh You. Hi, Stan.\n\n"))) ; empty lines at the end of files

  (testing "resource with partials"

    (is (=
      (render-template "i/t" {} :incpath "i")
      "All good in the hood.\n\n"))

    (is (=
      (render-template "i/t" {} :incpath ["i"])
      (render-template "t" {} :incpath "i")
      (render-template "i/t" {} :incpath ["i" "a" "b" "c"]))))

  (testing "not found thrown?"
    (is (thrown? Exception (render-template "does-not-exist" {})))))


