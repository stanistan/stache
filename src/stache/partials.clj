(ns stache.partials
  (:use [stache.helpers]
        [stache.dir]))

(def pattern
  "The pattern used to define partials"
  #"\{\{\>([\w\/-]+)\}\}")

(defn find-partials-in-template
  "Gets the defined partials in the current mustache string. Returns vector."
  [m]
  (map #(keyword (second %)) (re-seq pattern m)))

(defn- merge-partials-fn
  [partials incpath]
  (fn [k]
    (if (get partials k)
      nil
      {k (get-template (name k) incpath)})))

(defn get-partials
  "Gets a map of {:partial-name partial-content} recursively."
  ([m] (get-partials m {} ""))
  ([m partials] (get-partials m partials ""))
  ([m partials incpath]
    (let [p (find-partials-in-template m)
          gp (merge-partials-fn partials incpath)
          found (if (empty? p) [] (filter not-nil? (map gp p)))
          all (merge partials (reduce merge found))]
      (reduce merge all (map #(get-partials (second (first %)) all incpath) found)))))
