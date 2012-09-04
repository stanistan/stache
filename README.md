# stache

Automatically load your mustache templates!

Uses the [clostache](https://github.com/fhd/clostache) library as a parser.

*Note:* See tests for usage.

## Usage

The easiest way to add Stache to your project is using [clojars](https://clojars.org/stache).

##### leiningen

```clj
[stache "0.1.1"]
```

##### maven

```xml
<dependency>
  <groupId>stache</groupId>
  <artifactId>stache</artifactId>
  <version>0.1.1</version>
</dependency>
```

## Examples

Simplest case -- exactly the same as clostache.

```clj
(use 'stache.core)

(render-template "Hi, {{name}}." {:name "Stan"})
;=> "Hi, Stan."
```

#### Configuration

By default, stache looks for mustache templates with the suffix `.mustache` and
path `path/to/resources/templates`.

```clj
(use 'stache.config)

(config :path-to "mypath" :suffix ".tpl")

path-to-templates
;=> path/to/resources/mypath

template-suffix
;=> ".tpl"

; You can also reset to the default values
(reset-config)
```

#### Auto Loading Templates

Stache will look in each subdirectory `config/path-to-templates` for `mytemplate.mustache`
if not found, an exception will be thrown.
Stache will check for partials in the template (recursively) and render
the template using clostache.

In this scenario we are assuming the template `mytemplate.mustache` exists:

```
Let's pretend that this was parsed by {{name}}, and this is something from partials.
{{>list}}
```

`list.mustache` exists within either the main path, or `sub1`, `sub2`, or `sub3`:

```
{{#repeat}}
    <li>{{.}}</li>
{{/repeat}}
```

---

```clj
(use 'stache.core)

(let [inc ["sub1" "sub2" "sub3"] ; coll of subdirectories
      data {:name "Stan" :repeat ["a" "b" "c"]}]
  (render-template "mytemplate" data :incpath inc))
;=> "Let's pretend this was parsed by Stan, and this is something from partials.
;=>  <li>a</li>
;=>  <li>b</li>
;=>  <li>c</li>"
```

## Todo

- ~~Fix loading of partials with relative paths to the main template.~~
- ~~Write more tests for changing configs and usage.~~
- Disallow `/` in partial names.
- Instead of using config have `(make-parser)` return a parser mustache function to reduce state deps.
- Allow for changing of delimiters currently, only `{{}}` is supported.

## License

Copyright © 2012 Stan Rozenraukh

Distributed under the Eclipse Public License, the same as Clojure.
