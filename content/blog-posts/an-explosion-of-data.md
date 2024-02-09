:page/title An Explosion of Data
:blog-post/published #time/ldt "2024-01-03T15:45:00"
:blog-post/tags [:datomic :clojure]
:blog-post/series {:series/id :datomic}
:blog-post/original

{:blog "Parenteser"
 :url "https://parenteser.mattilsynet.io/smak-av-datomic/"}

:blog-post/description

Datomic is truly a delightful database to work with. I'm starting the year with
a new series of tidbits from this functional, functional database. First out is
the data model at its core -- and explosions!

:open-graph/description

Datomic is truly a delightful database to work with. First out in this blog post
series is the data model at its core -- and explosions!

:blog-post/body

Datomic is truly a delightful database to work with. I'm starting the year with
a new series of tidbits from this functional, functional database. First out is
the data model at its core -- and explosions!

#### Did you say explosions?

Yes, indeed!

Datomic stores all its data flat, but the data we work with is seldom like that.
Let's take an example:

```clj
{:blog-post/id 14
 :blog-post/title "Datomic Tidbits: An Explosion of Data"
 :blog-post/tags [:datomic :clojure]
 :blog-post/author {:person/id "magnars"
                    :person/given-name "Magnar"
                    :person/family-name "Sveen"}}
```

Here we have a map, with a list and another map inside. We could perhaps store
this exactly as is in a document database? -- but Datomic isn't one of those.
Besides, I suspect that the person mentioned is the author of other blog posts
as well.

We must break it down in some way.

#### So, explosions?

Yes, but first a bit about data modeling.

Traditional relational databases [struggle to model tree
structures](https://en.wikipedia.org/wiki/Object–relational_impedance_mismatch).
Datomic swung around the whole problem by instead basing itself on ideas from
universal data modeling, like
[RDF](https://en.wikipedia.org/wiki/Resource_Description_Framework)'s [semantic
triples](https://en.wikipedia.org/wiki/Semantic_triple).

Instead of rows and columns in square tables, the data is organized into
entities and attributes.

It looks almost like this:

```clj
[entity, attribute, value]
```

For example:

```clj
[1234, :blog-post/title, "Datomic Tidbits: An Explosion of Data"]
```

Datomic's innovation in this space is to add *transaction* to this triplet. It
allows us to track *when* this information was from. What other information was
asserted at the same time? Who provided it?

```clj
[entity, attribute, value, transaction]
```

The triplet has become a **datom**.

#### I believe I was promised explosions

They're coming now!

We had this, right?

```clj
{:blog-post/id 14
 :blog-post/title "Datomic Tidbits: An Explosion of Data"
 :blog-post/tags [:datomic :clojure]
 :blog-post/author {:person/id "magnars"
                    :person/given-name "Magnar"
                    :person/family-name "Sveen"}}
```

Datomic explodes this into its smallest components so that everything can be
stored flat. Let's start from the innermost structure:

```clj
[5678, :person/id, "magnars"]
[5678, :person/given-name, "Magnar"]
[5678, :person/family-name, "Sveen"]
```

Here we have an entity with an internal ID `5678`, and everything we know about
it.

```clj
[1234, :blog-post/id, 14]
[1234, :blog-post/title, "Datomic Tidbits: An Explosion of Data"]
[1234, :blog-post/author, 5678]
```

That's a neat trick. The blog post has a reference to another entity. In
Datomic, there's a schema describing which attributes point to other entities,
called *refs*.

Finally, the list of tags:

```clj
[1234, :blog-post/tags, :datomic]
[1234, :blog-post/tags, :clojure]
```

Something strange is going on here. Two values for the same attribute?

Again, we use Datomic's schema to say that this attribute has high cardinality
-- meaning it can have multiple values per attribute.

And with that, our nested data structure has been exploded into triples:

```clj
[1234, :blog-post/id, 14]
[1234, :blog-post/title, "Datomic Tidbits: An Explosion of Data"]
[1234, :blog-post/author, 5678]
[1234, :blog-post/tags, :datomic]
[1234, :blog-post/tags, :clojure]
[5678, :person/id, "magnars"]
[5678, :person/given-name, "Magnar"]
[5678, :person/family-name, "Sveen"]
```

Beautiful.

#### And the point is?

The main point is that this approach models both graphs and relational data --
completely free from [impedance
mismatch](https://en.wikipedia.org/wiki/Object–relational_impedance_mismatch).

Additionally, once you've exploded the tree structure, the data becomes much
easier to work with under the hood. They all have the same form, which opens up
many possibilities. For example, you can use it to [find differences between
arbitrary datasets efficiently](https://github.com/magnars/datoms-differ).

Next time we'll take a look at how Datomic uses this to efficiently index all
the data in the database.
