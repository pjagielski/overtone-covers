# overtone-covers

Collection of songs covered by [Overtone](https://github.com/overtone/overtone) and [Clojure](https://github.com/clojure/clojure).

## Requirements

Install [lein](https://github.com/technomancy/leiningen)

Install [jack](https://github.com/overtone/overtone/wiki/Installing-and-starting-jack) (linux only)

## Playing

### Calvin Harris - Let's Go

`lein repl`

```clojure
(use 'overtone.live)
(use 'overtone-workshop.letsgo)
(play-all (metronome 128))
(stop)
```

## Inspirations

* [Syntorial](http://www.syntorial.com/)
* [Calvin Harris "Lets Go" - Making The Beat](https://www.youtube.com/watch?v=wtGtnshXIU0)

## License

Copyright © 2015 Piotr Jagielski

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
