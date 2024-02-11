:page/title A Little Clojure Trick to Brighten Your Day
:blog-post/published #time/ldt "2019-10-02T09:00:00"
:blog-post/tags [:clojure :functional-programming]
:blog-post/original

{:blog "Kodemaker"
 :url "https://www.kodemaker.no/blogg/2019-10-clojure-update/"}

:blog-post/description

The most prominent and central ideas in Clojure might be pure functions and
immutable data, but today I write about a little thing -- just a neat little
feature -- that puts a smile on my face.

:blog-post/body

The most prominent and central ideas in Clojure might be pure functions and
immutable data, but today I write about a little thing -- just a neat little
feature -- that puts a smile on my face.

Here is a small code snippet:

```clj
(defn add-new-zombie [zombies]
  (conj zombies (create-zombie)))

(update game :zombies add-new-zombie)
```

The `update` function takes a map `game`, a key `:zombies` in that map, and a
function `add-new-zombie` that is used to update the value behind that key.

(`conj` adds a value to a list)

This `add-new-zombie` function doesn't seem to carry its own weight. We prefer
[small building blocks over small functions](/old-trick-new-context/).

Let's bring the code in via an anonymous function instead:

```clj
(update game :zombies #(conj % (create-zombie)))
```

This might look strange if you're not familiar with Clojure. There's all sorts
of syntax, what with hashes and percent signs.

We can forgo the shorthand and write a proper function literal instead:

```clj
(update game :zombies (fn [zombies] (conj zombies (create-zombie))))
```

Comparing the two, you might see that `%` acts as a sort of anaphoric parameter
-- special syntax for winning code golf tournaments. While sometimes handy, that
is not the thing that brings me joy when I code.

## Rather, it's this

The usual form of `update` looks like this:

```clj
(update map key function)
```

The joyous part is update's other form. It takes varargs and looks like this:

```clj
(update map key function args...)
```

These extra arguments are passed to the function. And the trick is: the value to
be updated is passed in **first**, followed by the rest of the arguments.

So we can change this:

```clj
(update game :zombies (fn [zombies] (conj zombies (create-zombie))))
```

to this:

```clj
(update game :zombies conj (create-zombie))
```

Read: "Update the game's zombies by adding a new zombie."

Beautiful.

## But that's not all

This trick also works with `update-in` and `swap!`. The latter updates Clojure's
atoms, a sort of mutable state containment facility.

We don't need to write:

```clj
(swap! game-atom (fn [game] (update game :zombies (fn [zombies] (conj zombies (create-zombie))))))
```

That's a mouthful. So many parentheses, right? We know that we can already
simplify it to:

```clj
(swap! game-atom (fn [game] (update game :zombies conj (create-zombie))))
```

Or, we can simplify it to:

```clj
(swap! game-atom update :zombies (fn [zombies] (conj zombies (create-zombie))))
```

But here is the most beautiful part. Since these compose absolutely wonderfully,
it all boils down to:

```clj
(swap! game-atom update :zombies conj (create-zombie))
```

It almost brings a tear to my eye.
