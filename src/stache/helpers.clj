(ns stache.helpers
  (:require [clojure.string :as string]))

(defn prep-path
  "Slits a string on a path separator, removes dups.
   Example:
   //a/b///c/d/e/fff// -> a/b/c/d/e/fff"
  [s]
  (string/join "/" (filter #(not (empty? %)) (string/split s #"\/"))))

(defn prep-paths
  "Joins prepped paths."
  [& paths]
  (->> paths (map prep-path) (string/join "/") (prep-path)))

(defn not-nil?
  "(not (nil?))"
  [n]
  (not (nil? n)))

(defn has-match
  "Returns bool for re-find"
  [rgx s]
  (not-nil? (re-find rgx s)))

(defmacro prr
  "For debugging, prints to out and returns the evaluated form."
  [& args]
  `(let [r# ~@args]
    (println r#)
    r#))
