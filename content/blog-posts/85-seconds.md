:page/title The Hunt for the Missing 85 Seconds
:blog-post/published #time/ldt "2023-12-12T09:00:00"
:blog-post/tags [:observability :ops]
:blog-post/original

{:blog "Parenteser"
 :url "https://parenteser.mattilsynet.io/85-sekunder/"}

:blog-post/description

Don your detective hat and join me in a desperate search for criminally long
response times.

:blog-post/body

"Magnar, the server is lagging again." The message ticked in from one of the
players of [my game](https://www.adventur.no).

Hmm... *again?* I opened the server dashboard and looked over the numbers.
Everything seemed fine. Plenty of memory available. No stress on the CPU. 95th
and 99th percentiles on response times under 50 milliseconds.

"Network trouble," I concluded. I sent back: "Are you on a bit of an unstable
network, maybe?" Yes, that could very well be it, was the answer. He was on his
mobile, after all. Great, then everything was as it should be.

This was six months ago.

Everything was not as it should be.

## Growing Concerns

In the following months, I received a few small drips of odd incidents. I got an
error message that a player had chosen an option that didn't exist. How could
that happen? Perhaps two requests collided due to poor connectivity? Not
impossible. Not impossible at all.

Occasionally, a player would report that "the server is lagging." Maybe about
once a month? My monitoring revealed nothing. The server had low load and plenty
of memory. Again; network trouble.

But every convenient "network trouble" conclusion brought with it a nagging
feeling. So, I decided to eliminate it as an excuse. Sure, poor internet could
explain the problems, but I needed proof.

## Client-Side Tracking

The client already had a fairly robust handling of errors and timeouts for
requests. It would retry multiple times, waiting a bit longer after each
failure, and conclude by asking the player to try again later.

My problem was that I only saw the story from the server's perspective. I only
saw the requests that actually made it through, and in the order they arrived at
the server. The story on the client side could be quite different.

I decided to create a package of log messages for the entire attempt --
including all requests. If the client had a bad experience, it would send the
package to the server at the first opportunity, which would then trigger an
email to me.

Already on the first day with this in production, I received an email. And one
thing became very clear:

It was not network trouble.

## 85 Seconds

The first request reached the server as intended, but the client gave up waiting
after a 4-second timeout and tried again.

The server responded: "Wait, I'm working on it."

The client waited a bit, then tried again.

"Wait, I'm working on it."

Again.

"Wait, I'm working on it."

Again.

"Wait, I'm working on it."

Again. And again. And again. And again. Until the client gave up.

The player was advised to try again a little later. Click! Try again.

"Wait, I'm working on it," said the server.

*It was still working on the first request.*

In fact, the server would continue working on the first request for an
incredible **85 seconds**. No wonder I got messages saying "the server is
lagging."

## There's Something Wrong That Isn't Right

I sat down and tried playing the game myself. Everything was lightning-fast,
just like all the other times I had tried. All dashboards still showed clear
skies and well under 50 milliseconds on average, 95th, and 99th percentile
response times.

I SSH-ed into the server, ran `top`, and looked at the numbers skeptically. I'm
no expert in interpreting `top`, but it seemed pretty good to me. In
desperation, I asked ChatGPT to interpret the top header:

> *In summary, this system is very stable, with extremely low CPU and memory
> pressure.*

Yes, that's what I thought too. Still no closer to a solution, though.

So what was special about this particular request?

<img class="floaty-photo" src="/images/usual-suspects.jpg">
I thought about the
most costly operations in the game: It's quite demanding when the adventurer
jumps far back in time... But no, it was a completely normal option selected
here.

Another costly operation is when the adventurer stays overnight because then all
the leaderboards are updated... Nope, it wasn't that either.

I went through absolutely everything I could think of. I scrutinized the code. I
looked in logs. In the end, I was left with only one explanation. It had to be
an insanely long Garbage Collection.

I upgraded to a new version of the JDK with a better GC algorithm and reduced
the server's memory, hoping not to receive more emails.

> *"Hope... Ha! Such a tease!"*
>
> -- Raphael, Baldur's Gate 3

## A Brief Detour

<img class="floaty-photo" src="/images/obseng.jpg">

At my job at the Norwegian Food Safety Authority, we've set aside half a day
every other week for self-directed professional development. My colleague
[Christian](https://cjohansen.no) was excited about the book [Observability
Engineering](https://www.honeycomb.io/wp-content/uploads/2022/05/Honeycomb-OReilly-Book-on-Observability-Engineering.pdf),
so I started reading.

The first four chapters discuss how metrics and logs fall short in complex
systems. They provide insufficient insight into what's actually happening across
our processes. One ends up having to rely on intuition and heuristics based on
past experiences with the system.

Yes, I could certainly relate.

The book suggests an alternative approach based on *traces* and *spans*, aiming
to achieve what they call *observability*: The ability to understand what's
happening in the system, whether or not one has ever encountered the situation
before, no matter how bizarre it might be.

## Observability

I believe I'll be writing more about observability on this blog, but let me
quickly explain:

- **Spans**, much like log messages, are snippets of information about what's
  happening in a process.

The difference is that they are structured data over a period of time. They have
a `timestamp` and `duration`, and all relevant information one would include in
a log message.

Additionally, they have a `parent` – that is, they are organized in a tree
structure.

- **Traces** are a collection of such spans and represent one request into the
  system, across clients, servers, backends, and microservices.

You achieve observability when you collect these in large enough quantities, and
with tools that help you find patterns in these vast data sets. This wasn't what
I did to find the 85 seconds, but the book had given me an idea.

## Homebrew Spans

It wasn't the Garbage Collection, I quickly realized. I continued receiving
emails about insane response times, despite the new JDK. Yet, all hope was not
lost.

The idea of a tree structure of *spans* instead of log messages gave me a new
approach to identifying my problem. I cobbled together a
[`with-span`](https://gist.github.com/magnars/849cdc6e0b6f0cc109f38c912ddc7144)
macro in Clojure, which I used like this:

```clj
(telemetry/with-span 'update-rankings {:player-id player-id}
  (update-rankings conn player-id))
```

Here, I give the span a name, throw in relevant parameters, and the macro
ensures the timestamp, duration, and tree structure are correct.

I also threw together a little UI. Here's an example. You can see the tree
structure of spans, and how long each one took.

<img src="/images/spans1.png" style="max-width: 100%">

As you can see, the time for parent spans is composed of the time of its child
spans.

So... what did the 85-second one look like?

Hold on tight.

<img src="/images/spans2.png" style="max-width: 100%">

Come again?

The timeline is completely dominated by the 85 seconds, but there are no child
spans that will own up to the time...?

A bit further down, we see something more:

<img src="/images/spans3.png" style="max-width: 100%">

What did you say? *misc*??

Again, none of the children take any time.

## In The End It Was Just My Fault After All

Because it wasn't network trouble. Nor was it Garbage Collection. Of course, it
wasn't. They're far too obvious villains to be the actual culprit.

Hidden between the lines lay the code I hadn't considered. Hadn't imagined could
be the error. Didn't even look twice at. *I had so thoroughly disregarded it that I
didn't even bother to create a span for it.*

Why is that?

Because it was a feature that I had turned off three years ago.

When I finally wrapped a `with-span` around it too, it glared back at me with
its endlessly long 85 seconds.

Here's how it went down: About 1 in 25,000 requests managed the feat of
running this old code -- *to no effect* -- (since the feature was turned off) and
fetched massive amounts of data from the database to generate... a discontinued
leaderboard.

What kind of leaderboard, you ask?

Well, it was the "Top 100 Speed Demons" leaderboard.

Of course it was.
