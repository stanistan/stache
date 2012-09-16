(defproject stache "0.2.0"
  :description "Easier {{ mustache }} templates."
  :url "https://github.com/stanistan/stache"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:test {:resource-paths ["test/resources"]}}
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojars.stanistan/clostache "1.4.0-dev"]])
