(ns stache.core
  (:use [clostache.parser :as parser]
        [stache.dir]
        [stache.helpers]
        [stache.partials]))

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
  (let [m (if (mustache? m) m (get-template m))
        partials (get-partials m partials incpath)]
    (parser/render m data partials)))
