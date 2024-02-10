:page/title An Old Trick in a New Context
:blog-post/published #time/ldt "2019-07-17T09:00:00"
:blog-post/tags [:design :clojure :functional-programming]
:blog-post/original

{:blog "Kodemaker"
 :url "https://www.kodemaker.no/blogg/2019-07-gammelt-triks-ny-kontekst/"}

:open-graph/description

It's easy -- maybe too easy? -- to bring old tricks into a new daily routine. In
this blog post, I briefly write about one of the things I've gradually
unlearned.

:blog-post/description

It's easy -- maybe too easy? -- to bring old tricks into a new daily routine.
The first Clojure code I wrote doesn't particularly resemble what I write today.
In this blog post, I briefly write about one of the things I've gradually
unlearned.

:blog-post/body

It's easy -- maybe too easy? -- to bring old tricks into a new daily routine.
The first Clojure code I wrote doesn't particularly resemble what I write today.
In this blog post, I briefly write about one of the things I've gradually
unlearned.

### Many Small Functions

It's been a few years now, but I learned from an old uncle that one must break
code up into many small functions. This made the code easier to understand, and
the function names helped document the code.

I don't think it's such a bad idea. I still use function names as documentation
instead of comments. However, I've discovered that this tip doesn't hold up as
well in Clojure.

Let me demonstrate. What does this code snippet do?

```clj
(decrease-player-health game)
```

It certainly looks like it reduces the health of the player in `game`. I know
this because I assume the function is well-named. We can certainly stick to that
assumption. Functions change, can be poorly named, etc., but that's not my point
here.

Let's say the function is implemented like this:

```clj
(defn decrease-player-health [game]
  (update game :player decrease-health))
```

Ah, look here, this seems correct. `:player` in `game` is updated with the
`decrease-health` function. We can assume it's also well-named, or we can click
further to its definition:

```clj
(defn decrease-health [player]
  (update player :health dec))
```

Perfect. Now we know what our `decrease-player-health` function does. It updates
`:health` on `:player` by calling `dec`, a function that decreases a number by
one.

### Many Small Building Blocks

Or was it perfect?

I had to read and understand two function names and navigate twice to verify
that the functions did what they said on the tin. [Naming is
hard](https://martinfowler.com/bliki/TwoHardThings.html).

What happens if we inline the `decrease-health` function?

```clj
(defn decrease-player-health [game]
  (update game :player #(update % :health dec)))
```

It looks a bit clumsy, but this form of nested updating can in Clojure be
written as:

```clj
(defn decrease-player-health [game]
  (update-in game [:player :health] dec))
```

And once that's done, do we need the `decrease-player-health` function?

Instead of:

```clj
(decrease-player-health game)
```

isn't it just as easy to read:

```clj
(update-in game [:player :health] dec)
```

?

No! It's *much easier* - provided that I'm familiar with the building blocks in
clojure.core. I don't need to read and understand a name. I don't need to
navigate to the code to be sure.

In other words: When the building blocks are good enough, readability can be
improved by avoiding named functions.

> It is better to have 100 functions operate on one data structure than 10
> functions on 10 data structures.
>
> -- [Alan Perlis](http://www.cs.yale.edu/homes/perlis-alan/quotes.html)
