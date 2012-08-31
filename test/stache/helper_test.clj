(ns stache.helper-test
  (:use stache.helpers clojure.test))

(deftest paths
  (testing "paths"

    (is (=
      (prep-path "///a/b///c/d/e/fff//")
      "a/b/c/d/e/fff"))

    (is (=
      (prep-path "")
      "")))

  (testing "multiple paths"

    (is (=
      (prep-paths "" "")
      ""))

    (is (=
      (prep-paths "/some/path/to/place/" "/continued/too//")
      "some/path/to/place/continued/too"))))

(deftest others

  (testing "not-nil"
    (is (= (not-nil? nil) false))
    (is (= (not-nil? 'a) true))
    (is (= (not-nil? "a") true))
    (is (= (not-nil? 1))))

  (testing "has-match"
    (is (= (has-match #"a" "a") true))
    (is (= (has-match #"[a-z]+" "123") false))))
