(ns stache.core
  (:use [clostache.parser :as parser])
  (:require [clojure.java.io :as io]))

;; Constants..............................................................................

(def config-default
  {:path-to "templates"
   :suffix ".mustache"})

(def partial-pattern #"\{\{\>([\w\/-]+)\}\}") ; the pattern used to define partials

;; Settings and Configs...................................................................

(declare path-to) ; the resource path where mustache templates are
(declare suffix)  ; suffix used for the files

(defn config
  "Bind values to path-to or suffix.
   Usage: (config :path-to \"path/to/mustaches/\" :suffix \".m\")"
  [& e]
  (let [e (apply hash-map e)
        p (:path-to e)
        s (:suffix e)]
    (do
      (and p (def path-to p))
      (and s (def suffix s)))))

(defn reset-config
  "Resets the settings to defaults."
  []
  (apply config (flatten (vec config-default))))

; set up default bindings
(reset-config)

;; Helpers................................................................................

(defn not-nil? [n]
  (not (nil? n)))

(defn has-match [rgx s]
  (not-nil? (re-find rgx s)))

;; Directory Utitlies.....................................................................

(defn mustache-path
  "Gets a resource path to the mustache file"
  [m]
  (str path-to "/" m suffix))

(defn get-template
  "Gets the contents of the mustache file"
  [m]
  (let [p (mustache-path m) r (io/resource p)]
    (if (nil? r)
      (throw (Exception. (str "mustache not found: " m " at path: " p)))
      (slurp r))))

;; Partials Utilities.....................................................................

(defn find-partials
  "Gets the defined partials in the current mustache string. Returns vector."
  [m]
  (mapv #(keyword (second %)) (re-seq partial-pattern m)))

(defn get-partials
  "Gets a map of {:partial-name partial-content} recursively."
  ([m] (get-partials m {}))
  ([m partials]
    (let [p (find-partials m)
          gp (fn [k] (if (get partials k) nil {k (get-template (name k))}))
          found (if (empty? p) [] (filter not-nil? (map gp p)))
          all (merge partials (reduce merge found))]
      (reduce merge all (map #(get-partials (second (first %)) all) found)))))


;; Mustache Utilities.....................................................................

(defn mustache?
  "Check to see if the given arg is a mustache string"
  [m]
  (cond (has-match #"\{\{" m) true
        (has-match #"\<" m) true
        :else false))

(defn render-template
  "Render the given mustache template with the data given.
   Finds partials automatically unless they are specified using:
   (render-template m data :partials {:name partial})"
  [m data & e]
  (let [e (apply hash-map e)
        m (if (mustache? m) m (get-template m))
        partials (get-partials m (or (:partials e) {}))]
    (parser/render m data partials)))
