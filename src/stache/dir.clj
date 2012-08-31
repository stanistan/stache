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
    (let [incpath (prep-incpath incpath)]
      (loop [p (first incpath)
             r (rest incpath)]
        (let [file-path (get-file-path m p)
              re (io/resource file-path)]
          (cond (not-nil? re) re
                (empty? r) nil
                :else (recur (first r) (rest r))))))))

(defn get-template
  ([m] (get-template m ""))
  ([m incpath]
    (let [resource (get-template-resource m incpath)]
      (if (nil? resource)
        (throw (Exception. (str "Mustache template not found [" m "]")))
        (slurp resource)))))
