# overtone-covers

Collection of songs covered by [Overtone](https://github.com/overtone/overtone) and [Clojure](https://github.com/clojure/clojure).

## Requirements

Install [lein](https://github.com/technomancy/leiningen)

Install [jack](https://github.com/overtone/overtone/wiki/Installing-and-starting-jack) (linux only)

## Playing

`lein repl`

### Calvin Harris - Let's Go

```clojure
(use 'overtone.live)
(use 'overtone-workshop.letsgo)
(play-all)
(stop)
```

### Ellie Goulding - Lights

```clojure
(use 'overtone.live)
(use 'overtone-workshop.lights)
(play-all)
(stop)
```


## Inspirations

* [Syntorial](http://www.syntorial.com/)
* [Calvin Harris "Lets Go"](https://www.youtube.com/watch?v=wtGtnshXIU0)
* [Ellie Goulding "Lights"](https://www.youtube.com/watch?v=A_TiZhgQ9Fw)

## License

Copyright © 2015 Piotr Jagielski

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
