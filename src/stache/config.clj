(ns stache.config)

(def config-default
  "Default configuration options for what we're looking for, and where"
  {:path-to "templates"
   :suffix ".mustache"})

; the resource path where mustache templates are
(declare path-to-templates)

; suffix used for the files
(declare template-suffix)

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
