:page/title A Deconstructed Database
:blog-post/published #time/ldt "2024-01-30T09:00:00"
:blog-post/tags [:datomic]
:blog-post/series {:series/id :datomic}
:blog-post/original

{:blog "Parenteser"
 :url "https://parenteser.mattilsynet.io/en-dekonstruert-database/"}

:blog-post/description

Rich Hickey once said that design fundamentally involves taking things apart so
that they can be put back together again. This is certainly true for Datomic, a
database split right down the middle.

:blog-post/body

In a [previous tidbit](/an-explosion-of-data/), we discussed how Datomic
differs from traditional databases in the way it models data - not in tables
with rows and columns, but in terms of entities and attributes. Today's tidsbit
addresses an entirely different aspect, where Datomic also takes a radically
different approach.

Rich Hickey [once said](https://www.youtube.com/watch?v=QCwqnjxqfmY) that design
fundamentally involves taking things apart in such a way that they can be put
back together. This is very much the case for Datomic's operational
architecture.

Traditional databases as we know them consist of a running process -- the server
-- through which all database activity passes. The server is the archivist, an
independent actor who guards and tends to the data. As a client of the database,
you send requests to the server -- often in the form of SQL -- and then the
archivist runs off and fetches exactly the snippets of data you requested or
updates the fields you wanted changed.

This is not the case with Datomic.

Instead of a server/client architecture, Datomic uses a transactor/peer
architecture.

- The **Transactor**'s sole task is to write new transactions with data.
- **Peers** read data.

Peers are named as such because they are not clients of a server, but are equal,
fully-fledged readers. They have as good access to the data as any other peer,
including the transactor.

In short: Writing and reading are completely separated, done by different
processes. Datomic has taken the traditional role of a database server and split
it wholly in two.

## So, there are peers you ask for data?

No, but good question. Here's what's so cool: You are a peer. That is, the app
you're writing is. Your process, your code has direct access to the data. When
starting, it asks the transactor where the data is stored, and then it fetches
the data itself from there.

Let me try to illustrate what this looks like in practice:

We know that databases need indexes to be fast. The indexes are something the
server uses to look up data efficiently. But with Datomic, the indexes are not
something the server uses; they are something we use. We have direct access to
the indexes, in our own process.

Let's look at some code. The Datomic API provides access to the index via
`d/datoms`. It gives us a list of datoms that match:

```clj
(count (seq (d/datoms db :avet :player/name)))

;; => 8324
```

Here, we use the :avet index, which is sorted by **a**ttribute first, then
**v**alue, **e**ntity, and **t**ransaction. We find all entries with the
`:player/name` attribute, and learn that there are 8324 players with names in
the database.

Let's look at the beginning of the index:

```clj
(take 5 (d/datoms db :avet :player/name))

;; =>

(#datom[17592205630297 65 "A Dutch Curious" 13194159119207]
 #datom[17592186090739 65 "Aaa" 13194139579706]
 #datom[17592188248201 65 "Aaaa" 13194141737106]
 #datom[17592186187563 65 "Aaaaaa" 13194139676483]
 #datom[17592195932321 65 "Aaaaaaaaaahhhhh" 13194149421349])
```

It's clear that the index is sorted alphabetically, and that some players of
my game do not have the best imagination when it comes to names.

So, here are datoms, tuples of `[e a v t]` (more on this in [the previous
tidbit](/an-explosion-of-data/)). The attribute might be the hardest to recognize
because it is represented by its database ID, `65`, which is the sequence number
`:player/name` has been assigned.

So, let's find my player in this dataset:

```clj
(first (d/datoms db :avet :player/name "Magnar"))

;; => #datom[17592202810723 65 "Magnar" 13194156299671]
```

A quick lookup in the index, and I found my ID.

If I already had an ID, and instead wondered what the name was, I can use the
index that is sorted by entity ID, `:eavt`.

```clj
(first (d/datoms db :eavt 17592202810723 :player/name))

;; => #datom[17592202810723 65 "Magnar" 13194156299671]
```

As you can see, this gives the same datom, just looked up in a different index.
If I do the same lookup as above, but omit specifying the attribute, like
this ...

```clj
(d/datoms db :eavt 17592202810723)
```

... then I get a list of *all* attributes for my player.

Pretty cool.

## In Conclusion

In everyday use, one does not often directly use the indexes. Datomic has more
practical ways to retrieve data than this. But the example illustrates the
point: With Datomic, your application process gets full access to the data that
is normally thought to be hidden behind a query language and an external
process.

Not only does this mean that problems like
[N+1](https://docs.sentry.io/product/issues/issue-details/performance-issues/n-one-queries/)
disappear entirely. It also means you'll never again take down production
because you wrote a suboptimal SQL—you can spend as much CPU power and time on a
query as you want, without affecting anyone else's performance. It also means
you get much closer to the data. And that makes a big difference.
