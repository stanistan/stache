(ns stache.dir
  (:use [stache.helpers]
        [stache.config :as conf])
  (:require [clojure.java.io :as io]))

(defn get-file-path
  "Gets a resource path to the mustache file"
  ([m] (get-file-path m ""))
  ([m incpath]
    (-> (prep-paths conf/path-to-templates incpath)
      (str "/" m conf/template-suffix)
      (prep-path))))

(defn- prep-incpath
  "Makes sure that incpath is a sequence and has an empty string."
  [p]
  (let [p (if (coll? p) p [p])]
    (if (contains? (set p) "") p (conj p ""))))

(defn get-template-resource
  ([m] (get-template-resource m ""))
  ([m incpath]
    (reduce
      #(or %1 (io/resource (get-file-path m %2)))
      nil
      (prep-incpath incpath))))

(defn- not-found
  [m incpath]
  (format "Template [%s] not found in incpath [%s]" m incpath))

(defn get-template
  ([m] (get-template m ""))
  ([m incpath]
    (let [resource (get-template-resource m incpath)]
      (if (nil? resource)
        (throw (Exception. (not-found m incpath)))
        (slurp resource)))))
