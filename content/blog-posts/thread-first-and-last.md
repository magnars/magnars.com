:page/title Sewing S-Expressions with Thread-First and -Last
:blog-post/published #time/ldt "2020-06-10T09:00:00"
:blog-post/tags [:clojure :clojurescript]
:blog-post/original

{:blog "Kodemaker"
 :url "https://www.kodemaker.no/blogg/2020-06-thread-first-and-last/"}

:open-graph/description

When you're new to Clojure, the macros `->` and `->>` can be pretty confusing,
making the code look quite mystical. It won't be long now before you
fall in love and start using them everywhere.

:blog-post/description

When you're new to Clojure, the macros `->` and `->>` can be pretty confusing,
making the code look quite mystical. Fortunately, it won't be long now before
you fall in love and start using them everywhere.

:blog-post/body

When you're new to Clojure, the macros `->` and `->>` can be pretty confusing,
making the code look quite mystical. Fortunately, it won't be long now before
you fall in love and start using them everywhere.

## What They Look Like and How They Work

The macros `->` (thread-first) and `->>` (thread-last) let you rewrite deeply
nested code into something that looks more like imperative statements.

Take a look at these four code snippets:

```clj
(-> player
    (update :health dec)
    (dissoc :happy?)
    (assoc :bloodied? true))
```
```clj
(-> (update player :health dec)
    (dissoc :happy?)
    (assoc :bloodied? true))
```
```clj
(-> (dissoc (update player :health dec) :happy?)
    (assoc :bloodied? true))
```
```clj
(assoc (dissoc (update player :health dec) :happy?) :bloodied? true)
```

All of these expressions evaluate to the same thing, but most would argue that
the first is easier to read. That's exactly the point. Thread-first `->` allows
us to write code in a more readable manner, without affecting what the compiler
sees.

If you study the examples more closely, you'll see that `->` stitches together
the expression by placing the previous element into the next. Where? In the
first parameter position. Thread-first.

What about thread-last `->>`? Well, now you might be wondering.

## Thread Last

Here are some new code snippets:

```clj
(->> zombies
     (filter :aware-of-player?)
     (remove :dead?)
     (map move-towards-player))
```
```clj
(->> (filter :aware-of-player? zombies)
     (remove :dead?)
     (map move-towards-player))
```
```clj
(->> (remove :dead? (filter :aware-of-player? zombies))
     (map move-towards-player))
```
```clj
(map move-towards-player (remove :dead? (filter :aware-of-player? zombies)))
```

Again, the first one is easier to read. The operations are listed in the same
order as they are performed. The parentheses don't feel as overwhelming.

You can see that `->>` also stitches the expression together by placing the
previous element into the next, but this time in the last parameter position.

And that's really all you need to know. `->` and `->>` make your code more
readable by avoiding deep nesting. But if you stop reading here, you won't learn
about the secret in clojure.core.

## The Secret in clojure.core

When Rich Hickey was creating Clojure, he spent a lot of time in his hammock
pretending to sleep. There, he pondered a lot, and much good came out of that
thought work. One such gem is how the parameter lists of Clojure's core
functions are secretly designed with threading in mind:

- Functions that work with a map take it as the first argument.
- Functions that work with a seq take it as the last argument.

Think back to the examples above. When we worked with `player` (a map), it was
easy to use `assoc`, `dissoc`, and `update` with thread-first `->`. Why? Because
all these take the map as the first argument.

When we worked with `zombies` (a list), it was easy to use `filter`, `remove`,
and `map` with thread-last `->>`. Again, because all these take the seq as the
last argument.

I call it a secret because I haven't seen it explicitly written in the
documentation for Clojure anywhere, but all the functions work according to that
principle.

But what does it really mean?

## Clojure is Full of Affordances

One of the advantages of learning functional programming with Clojure is that it
forces your hand. Kotlin, Scala, and Groovy are all fine languages, yet they
often allow old habits to persist. When you come to Clojure, it's a hard stop.
You *must* learn to write functional code to get anywhere.

Clojure, therefore, has opinions on how you should write code. The language
makes it pleasant to do things 'right' and painful to do them 'wrong'.

My claim is that thread-first `->` and thread-last `->>` are set up as such an
affordance: Operations on the same data structure are easy to compose together,
but the moment you switch between a map and a seq, it becomes painful. You have
to switch threading. It's awkward.

Read: Don't do it.

Instead, break up the code. When you go from a map to a seq, break the
threading. When you go from a seq to a map, break up. It's okay to give it a
name. [Point-free programming](https://en.wikipedia.org/wiki/Tacit_programming)
is cool, but don't take it too far.

This way, you're going with the flow of Clojure.

## Playing Well with Others

Another very common reason threading becomes awkward is when your own functions
don't follow the same principle. It's not surprising that this happens, really.
It was a secret, after all. But now you're in on it.

Follow the same principle yourself. Accept maps as the first parameter and seqs
as the last, and things will flow better.

Look how nice:

```clj
(defn loot-bodies [player zombies]
  ...)

(-> player
    (update :health dec)
    (dissoc :happy?)
    (assoc :bloodied? true)
    (loot-bodies zombies))
```

Here we accept `player` first, and it plays along with `->`. The other way
around would have been awkward:

```clj
(defn loot-bodies [zombies player]
  ...)

(loot-bodies zombies
             (-> player
                 (update :health dec)
                 (dissoc :happy?)
                 (assoc :bloodied? true)))
```

Nope.

## Finally, Why Do We Need These?

It's an interesting question, because it's easy to think this comes down to
Clojure being a Lisp, you know, with all those parentheses. That's not the case.
When writing Emacs Lisp, for example, you write good old-fashioned imperative
code: A long series of statements one after the other.

No, the reason is that Clojure code is built up of [expressions - not
statements](https://fsharpforfunandprofit.com/posts/expressions-vs-statements/).
The reason is
[immutability](https://clojure.org/about/functional_programming#_immutable_data_structures).
Without a place to store intermediate state, you are forced to nest expressions.
That can quickly reduce readability.

Thread-first `->` and thread-last `->>` give much of that readability back.
