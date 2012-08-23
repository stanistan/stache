# stache

A Clojure library designed to make using Mustache templates easier.

Uses the [clostache](https://github.com/fhd/clostache) library.

Stache automatically finds partials in your markup.

*Note:* This needs to be fixed soon, with real usage.

## Usage

```clj
(use 'stache.core)

(render-template "Hi, {{name}}." {:name "Stan"})
;; "Hi, Stan."
```

## License

Copyright © 2012 Stan Rozenraukh

Distributed under the Eclipse Public License, the same as Clojure.
