(ns stache.core
  (:use [clostache.parser :as parser])
  (:require [clojure.java.io :as io]))

;; Constants..............................................................................

(def config-default
  "Default configuration options for what we're looking for, and where"
  {:path-to "templates"
   :suffix ".mustache"})

(def partial-pattern
  "The pattern used to define partials"
  #"\{\{\>([\w\/-]+)\}\}")

;; Settings and Configs...................................................................

(declare path-to-templates) ; the resource path where mustache templates are
(declare template-suffix)  ; suffix used for the files

(defn config
  "Bind values to path-to or suffix.
   Usage: (config :path-to \"path/to/mustaches/\" :suffix \".m\")"
  [& {:keys [path-to suffix]}]
  (do
    (and path-to (def path-to-templates path-to))
    (and suffix (def template-suffix suffix))))

(defn reset-config
  "Resets the settings to defaults."
  []
  (apply config (flatten (vec config-default))))

; set up default bindings
(reset-config)

;; Helpers................................................................................

(defn not-nil?
  [n]
  (not (nil? n)))

(defn has-match
  [rgx s]
  (not-nil? (re-find rgx s)))

;; Directory Utitlies.....................................................................

(defn mustache-path
  "Gets a resource path to the mustache file"
  [m]
  (str path-to-templates "/" m template-suffix))

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
  [m data & {:keys [partials]}]
  (let [m (if (mustache? m) m (get-template m))
        partials (get-partials m (or partials {}))]
    (parser/render m data partials)))
