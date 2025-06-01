:page/title Three Tiny Tidbits That Made Adding Municipalities Effortless
:blog-post/published #time/ldt "2024-02-09T09:00:00"
:blog-post/tags [:datomic :clojure]
:blog-post/series {:series/id :datomic}
:blog-post/original

{:blog "Parenteser"
 :url "https://parenteser.mattilsynet.io/kommuner-voila/"}

:blog-post/description

When we recently added individual pages for each municipality to the new food
safety poster site, I caught myself chuckling with satisfaction more than once.
It was a good day in the company of Datomic.

:blog-post/body

At work we recently threw together a new site for [food safety posters
online](https://smilefjes.mattilsynet.no/). There was a bit of a rush since an
on-prem server was about to be shut down, so it was extra satisfying to ship the
whole thing in a week. Just like this blog, we built a [static
site](/static-sites/) using [Christian](https://cjohansen.no)’s [Stasis
Powerpack](https://github.com/cjohansen/powerpack). That definitely helped with
the speed.

One thing the old site lacked was an overview of all restaurants by municipality.
Luckily, the Norwegian postal service has made [a list of all postal codes and
their corresponding
municipalities](https://www.bring.no/tjenester/adressetjenester/postnummer/postnummertabeller-veiledning)
publicly available. So we decided to quickly whip up some [municipality
pages](https://smilefjes.mattilsynet.no/kommune/fredrikstad/) at the last
minute. Normally this would mean a tangle of joins, transforms, and glue code --
but with Datomic, it turned out to be ridiculously easy.

## Tidbit 1 — The Import

We had already pulled in all the food safety inspections and their corresponding
restaurants from [the dataset on Data
Norge](https://data.norge.no/datasets/288aa74c-e3d3-492e-9ede-e71503b3bfd9). The
parsing looked (mostly) like this:

```clj
(let [m (zipmap csv-header csv-line)]
  {,,,
   :spisested/navn (:navn m) ;; spisested = restaurant :)
   :spisested/orgnummer (:orgnummer m)
   :spisested/poststed {:poststed/postnummer (:postnr m)}
   ,,,})
```

A `poststed` (postal place) is its own entity, and `:poststed/postnummer` is set
up as a unique identifier:

```clj
;; From the Datomic schema:

{:db/ident :poststed/postnummer
 :db/valueType :db.type/string
 :db/unique :db.unique/identity ;; <--
 :db/cardinality :db.cardinality/one}
```

Datomic hanldes identity attributes differently, so this line ...

```clj
 {:poststed/postnummer (:postnr m)}
```

... turns into an upsert. Meaning: if a `poststed` entity with this postal code already exists, it’s reused—otherwise, a new one is created.

And because the `poststed` is declared like this ...

```clj
 :spisested/poststed {:poststed/postnummer (:postnr m)}
```

... it gets automatically linked to the restaurant.

But wait — we don’t have any municipalities yet. Those come from the postal
service CSV file, which we import too, but in a separate step. It looks roughly
like this:

```clj
{:poststed/postnummer (:postnummer m)
 :poststed/navn (:poststed m)
 :poststed/kommune {:kommune/kode (:kommunekode m)
                    :kommune/navn (:kommunenavn m)}}

;; kommune = municipality
```

Again, we’re using upserts — two of them this time. If a `poststed` with that postal code already exists, it’s reused — but enriched with name and municipality. If a municipality with that code already exists, it’s reused as well.

And just like that, we’ve stitched together restaurants → via poststed → to
municipality — all through upserts, without me having to “do anything” to wire
it up.

Lovely.

**PS!** I realize I ought to write a little tidbit on Datomic’s delightful system for describing data transactions in this format. It’s coming!

## Snippet 2 — Where’s the URL?

As I’ve [written before](/an-explosion-of-data/), Datomic models its data as
entities and attributes — not tables. If you’re used to tables, it might feel
like all the entities are just floating around in an unstructured soup. But
that’s a good thing! The world isn’t rectangular.

If you read Christian’s [blog post on keys and how to use
them](https://cjohansen.no/keys/), you saw an example of this in action:

```clj
{:db/id 17592186046486
 :kommune/kode "3107"
 :kommune/navn "Fredrikstad"
 :page/uri "/kommune/fredrikstad/"
 :page/kind :page.kind/kommune-page}
```

Here we’ve got an entity that’s a municipality (`kommune`) in some contexts and
a page in others. Datomic lets you model that without breaking a sweat.

So when I was putting together this link:

```clj
[:a.hover:underline {:href "..."}
  (:kommune/navn kommune)]
```

…I caught myself wondering: “Okay, but what *is* the actual URL for a municipality page?”

I started thinking about writing a function like `(get-kommune-url kommune)` to figure it out.

And then it hit me:

```clj
[:a.hover:underline {:href (:page/uri kommune)}
  (:kommune/navn kommune)]
```

Haha! Sometimes things really *are* that simple.

Datomic's flexible data model lets entities serve multiple roles without extra
ceremony. I didn’t have to juggle multiple layers of abstraction — the data was
already where I needed it.

## Snippet 3 — The search

Later on, we wanted to include municipality names in the search on the front
page. The municipality name was a good search candidate alongside other
address-related fields in the index. Here’s the relevant code:

```clj
(defn get-searchable-address [spisested]
  (->> [(-> spisested :spisested/adresse :poststed)
        (-> spisested :spisested/adresse :linje1)
        (-> spisested :spisested/adresse :linje2)]
       (remove empty?)
       (str/join " ")))
```

Oh no, this function receives only `spisested` -- the restaurant. No database to
look up the municipality anywhere in sight.

Think for a second — how would *you* go about also passing the municipality down
to this function?

Maybe you’d have to add a JOIN to a SQL query elsewhere? In that case, you’d be
looking at a double join: from restaurant to postal place to municipality.

Maybe you’d add the municipality name to some kind of DTO or `Spisested` object?

Maybe you’d just pass both the restaurant and the municipality into the
function?

Okay, enough daydreaming. Here’s what we actually ended up with:

```clj
(defn get-searchable-address [spisested]
  (->> [(-> spisested :spisested/adresse :poststed)
        (-> spisested :spisested/adresse :linje1)
        (-> spisested :spisested/adresse :linje2)
        (-> spisested :spisested/poststed :poststed/kommune :kommune/navn)]
       (filter not-empty)
       (str/join " ")))
```

Ha!

Thanks to Datomic’s entity API — built on the [direct index
access](/a-deconstructed-database/) we’ve talked about before — the entire
database is seamlessly navigable.

No SQL. No unholy INNER JOINs. Just data.

And in the end, it wasn’t about clever code. It was about letting the data model
carry the weight.
