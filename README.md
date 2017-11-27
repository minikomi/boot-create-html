# boot-create-html

A Boot task to generate html from edn files.

## Usage

To use this in your project, add `[poyo.co/boot-create-html "0.1.1-SNAPSHOT" :scope "boot"]` to your `:dependencies`
and then require the task:

    (require '[poyo.co.boot-create-html :refer [create-html]])

Create a `.html.edn` file in `resources/` relative to where you'd like the HTML generated.
The edn should contain **at least** a `:template key which is a symbol referenceing the function required to generate the HTML.

## Options

``` clj
[d data-ext EXT str "The extension used for data files. defaults to .html.edn."
 e every-time bool "HTML should be build every time, regardless of no changes to namespaces / data edn."]
```

## License

Copyright © 2017 Adam Moore

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
