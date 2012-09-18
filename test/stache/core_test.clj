(ns stache.core-test
  (:use clojure.test
        stache.core
        clostache.parser))

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

  (testing "nested partials-stache"
    (let [tmplate  "Oh {{>nest-one}}"
          partials {:nest-one "This {{>nest-two}}"
                    :nest-two "Is {{>nest-three}}"
                    :nest-three "Nested"}]
      (is (=
        (render-template tmplate {} :partials partials)
        (render tmplate {} partials) ))))

  (testing "nested partials"
    (is (=
      (render-template "nested" {})
      "Oh This Is Nested\n\n\n\n")))

  (testing "nested with incpath"
    (is (=
      (render-template "main" {} :incpath "incpath")
      "Test Successful Recursive\n\n\n")))

  (testing "not found thrown?"
    (is (thrown? Exception (render-template "does-not-exist" {})))))

(deftest make

  ; Tests defparser
  (defparser test-parser :path-to "templates-other" :suffix ".m")

  (testing "test-parser"
    (is (=
      (test-parser "simple" {:hello "Hi"})
      "This is a test Hi.\n")))

  (defparser default-parser)

  (testing "default-parser"
    (is (=
      (default-parser "ps" {:name "Stan"})
      (render-template "ps" {:name "Stan"})))))


