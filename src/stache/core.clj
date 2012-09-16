(ns stache.core
  (:use [stache.config :as conf]
        [stache.dir :only [get-template]]
        [stache.helpers :only [has-match]]
        [stache.partials :only [get-partials]])
  (:require [clostache.parser :as parser :only [render]]))

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
  [m data & {:keys [partials incpath] :or {partials {} incpath ""}}]
  (let [m (if (mustache? m) m (get-template m incpath))
        partials (get-partials m partials incpath)]
    (parser/render m data partials)))

(defmacro defparser
  "Macro to define your own parser.
   Usage:
   (defparser my-parser :path-to \"some/path/to/templates\" :suffix \".m\")
   (my-parser \"template\" data)"
  [parser-name & args]
  (let [[to s] (apply conf/or-defaults args)]
    `(defn ~parser-name [& gs#]
      (with-redefs
        [stache.config/current-configuration (fn [] {:path-to ~to :suffix ~s})]
        (apply render-template gs#)))))
