(ns stache.core
  (:use [clostache.parser :as parser])
  (:require [clojure.java.io :as io]))

;; Constants..............................................................................

(def config-default
  [:path-to "templates"
   :suffix ".mustache"])

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
  (apply config config-default))

; set up default bindings
(reset-config)

;; Helpers................................................................................

(defn key-to-keyword
  "Makes a hash map with keyword keys from string keys."
  [h]
  (reduce merge (map #(hash-map (keyword (first %)) (second %)) h)))

(defn not-nil? [n]
  (not (nil? n)))

(defn has-match [rgx s]
  (not-nil? (re-find rgx s)))

;; Directory Utitlies.....................................................................

(defn mustache-path
  "Gets a resource path to the mustache file"
  [m]
  (str path-to "/" m suffix))

(defn get-template [m]
  "Gets the contents of the mustache file"
  (let [p (mustache-path m) r (io/resource p)]
    (if (nil? r)
      (throw (Exception. (str "mustache not found: " m " at path: " p)))
      (slurp r))))

;; Partials Utilities.....................................................................

(defn find-partials
  "Gets the defined partials in the current mustache string."
  [m]
  (map #(second %) (re-seq partial-pattern m)))

(defn- get-partials-content
  "Gets a map of {partial-name parial-content} recursively"
  [m partials]
  (let [p (find-partials m)
        found (if (empty p) []
                (filter not-nil?
                  (map #(if (get partials p) nil {p (get-template p)}) p)))
        all (merge partials (reduce merge found))]
    (reduce merge (map #(get-partials-content (second (first %)) all) found))))

(defn get-partials
  "Gets a map of partials in the given mustache, returns {:partial-name partial-content}"
  ([m] (get-partials m {}))
  ([m partials]
    (key-to-keyword (get-partials-content m partials))))

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
