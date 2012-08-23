# stache

A Clojure library designed to make using Mustache templates easier.

Uses the [clostache](https://github.com/fhd/clostache) library.

Stache automatically finds partials in your markup.

*Note:* See tests usage.

## Usage

```clj
(use 'stache.core)

(render-template "Hi, {{name}}." {:name "Stan"})
;; "Hi, Stan."
```

### Todo:

- Fix loading of partials with relative paths to the main template.
- Write more tests for changing configs and usage.
- Disallow `/` in partial names.

## License

Copyright © 2012 Stan Rozenraukh

Distributed under the Eclipse Public License, the same as Clojure.
