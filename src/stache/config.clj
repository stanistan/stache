(ns stache.config)

(def config-default
  "Default configuration options for what we're looking for, and where"
  {:path-to "templates"
   :suffix ".mustache"})

; the resource path where mustache templates are
(declare path-to-templates)

; suffix used for the files
(declare template-suffix)

(defn- current-configuration
  []
  {:path-to path-to-templates
   :suffix template-suffix})

(defn or-defaults
  [& {:keys [path-to suffix]
      :or {path-to (:path-to current-configuration)
           suffix (:suffix current-configuration)}}]
  [path-to suffix])

(defn config
  "Bind values to path-to or suffix.
   Usage: (config :path-to \"path/to/mustaches/\" :suffix \".m\")"
  [& args]
  (let [[p s] (apply or-defaults args)]
    (do
      (def path-to-templates p)
      (def template-suffix s)
      (current-configuration))))

(defn reset-config
  "Resets the settings to defaults."
  []
  (apply config (flatten (vec config-default))))

; set up default bindings
(reset-config)
