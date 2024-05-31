:page/title A Simple Frontend Architecture That Works
:blog-post/published #time/ldt "2020-01-29T09:00:00"
:blog-post/tags [:frontend :design :functional-programming]
:blog-post/original

{:blog "Kodemaker"
 :url "https://www.kodemaker.no/blogg/2020-01-enkel-arkitektur/"}

:blog-post/description

There are many grand frameworks to choose from for your frontend architecture,
but do you need all the moving parts? In this blog post, I will talk about a
simple architecture that has served me well.

:blog-post/body

There are many grand frameworks to choose from for your frontend architecture,
but do you need all the moving parts? In this blog post, I will talk about a
simple architecture that has served me well.

Here are the key points:

- All data is gathered in one place.
- The data flow is predictable and unidirectional.
- UI components have all their data passed to them.
- UI components are independent of domain and context.
- Actions are communicated from the UI components via data.
- The moving parts are centralized at the top level in a main function.

## In Short

The app is kicked off by a `main` method, which creates a place to gather data.
This data is fetched and sent to a `prepare` function that transforms domain
data into UI data. The UI data is then rendered using generic components.

<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="230 350 400 180" xml:space="preserve">
  <style>.st0{fill:#111;stroke:#bbb;stroke-miterlimit:10}.st1{fill:#bbb;font-family:&apos;ArialMT&apos;}.st2{font-size:10px}.st3,.st4{fill:none;stroke:#bbb;stroke-miterlimit:10}.st4{stroke-width:.75}</style>
  <path class="st0" d="M375.5 404.88h-42c-5.52 0-10-4.48-10-10v-15.75c0-5.52 4.48-10 10-10h42c5.52 0 10 4.48 10 10v15.75c0 5.52-4.48 10-10 10zM519.5 418.5H626V512H519.5z"/>
  <text transform="translate(343.663 391)" class="st1 st2">Main</text>
  <text transform="translate(250.935 446.614)">
    <tspan x="0" y="0" class="st1 st2">Domain-</tspan>
    <tspan x="10.83" y="11" class="st1 st2">data</tspan>
  </text>
  <ellipse class="st0" cx="270.88" cy="418.81" rx="30.75" ry="12.81"/>
  <ellipse class="st0" cx="270.88" cy="475.19" rx="30.75" ry="12.81"/>
  <path class="st3" d="M240.12 417.96v56.37M301.62 417.96v56.37"/>
  <path class="st4" d="M309 450.5h59.7"/>
  <path class="st1" d="M374 450.5l-7.46 3.05 1.77-3.05-1.77-3.05z"/>
  <text transform="translate(320.342 445.96)" class="st1 st2">prepare</text>
  <path class="st4" d="M428.5 450.5h74.7"/>
  <path class="st1" d="M508.5 450.5l-7.46 3.05 1.77-3.05-1.77-3.05z"/>
  <text transform="translate(442.896 444.96)">
    <tspan x="0" y="0" class="st1 st2">generic</tspan>
    <tspan x="-7.5" y="16" class="st1 st2">components</tspan>
  </text>
  <text transform="translate(387.18 435.96)" class="st1 st2">UI-data</text>
  <text transform="translate(564.91 435.96)" class="st1 st2">DOM</text>
  <g>
    <path class="st4" d="M323.5 394.5L305 409.63"/>
    <path class="st1" d="M301.5 412.5c1.15-1.96 2.34-4.68 2.7-6.82l1.26 3.58 3.26 1.94c-2.17-.07-5.07.56-7.22 1.3z"/>
  </g>
  <g>
    <path class="st4" d="M404.5 441.5l-14 30M404.02 442.71l20.48 29.79"/>
    <circle class="st1" transform="rotate(-27.365 404.043 442.822)" cx="404.1" cy="442.82" r="2.82"/>
    <path class="st4" d="M390.5 471.5l-14.43 16.56M390.5 471.5l13 15M403.5 486.5l-12 20M403.5 486.5l12 21"/>
  </g>
  <g>
    <path class="st4" d="M575.5 441.5l-14 30M575.02 442.71l20.48 29.79"/>
    <circle class="st1" transform="rotate(-27.365 575.032 442.833)" cx="575.1" cy="442.82" r="2.82"/>
    <path class="st4" d="M561.5 471.5l-14.43 16.56M561.5 471.5l13 15M574.5 486.5l-12 20M574.5 486.5l12 21"/>
  </g>
  <path class="st3" d="M519.5 422.59H626"/>
</svg>

## Data Flow

Your data reaches the client in some way. I won't go into details in this blog
post, other than to say that the components themselves should not be fetching it.
Maybe you're fetching data with GraphQL, or WebSockets, or some GET requests -
as long as it's done centrally, that's fine.

Once you have the data, it is kept in a top-level location defined by the main
function. This could be in [a database](https://github.com/tonsky/datascript),
in an [atom](https://clojure.org/reference/atoms), or if necessary, in a
JavaScript object.

Either way, you need to know when the data changes so that an update of the UI
can be initiated.

When this happens, a `prepare` function is called with all the data,
transforming domain data into UI data. This UI data is sent to a top-level
component, which renders the UI using generic components.

That's the entire data flow. When data changes, all of this happens again. The
[virtual DOM](https://github.com/snabbdom/snabbdom) trick (popularized by React)
allows us to do this without significant performance issues.*

<small class="tiny">* Out of the box for ClojureScript, but large JavaScript
projects might need to resort to immutable.js</small>

## Generic Components

These are our building blocks. They implement our design but are not aware of
the domain. They do not know the context in which they are used. They are
unaware of the actions performed when buttons are pressed.

This makes the components eminently reusable. When we move from a
`RegistrationButton` to a `PrimaryButton`, it can be used in many places. You
get a separate language for the design, detached from the domain language.

But what about actions? Shouldn't a `RegistrationButton` do something different
than a `SignInButton`? Yes, but the actions to be performed are also passed to
the components as data.

Simply put:

```js
PrimaryButton({action: ["register-user"]})
```

All `PrimaryButton` knows is that when it is pressed, the `action` should be put
on an event bus. This is monitored by the main function, which then carries out
the action.

<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="230 350 400 180" xml:space="preserve">
  <style>.st5{font-size:7px}</style>
  <path class="st0" d="M375.5 404.88h-42c-5.52 0-10-4.48-10-10v-15.75c0-5.52 4.48-10 10-10h42c5.52 0 10 4.48 10 10v15.75c0 5.52-4.48 10-10 10zM519.5 418.5H626V512H519.5z"/>
  <text transform="translate(343.663 391)" class="st1 st2">Main</text>
  <path class="st0" d="M509.5 404.88h-42c-5.52 0-10-4.48-10-10v-15.75c0-5.52 4.48-10 10-10h42c5.52 0 10 4.48 10 10v15.75c0 5.52-4.48 10-10 10z"/>
  <text transform="translate(466.264 391)" class="st1 st2">Event bus</text>
  <text transform="translate(250.935 446.614)">
    <tspan x="0" y="0" class="st1 st2">Domain-</tspan>
    <tspan x="10.83" y="11" class="st1 st2">data</tspan>
  </text>
  <ellipse class="st0" cx="270.88" cy="418.81" rx="30.75" ry="12.81"/>
  <ellipse class="st0" cx="270.88" cy="475.19" rx="30.75" ry="12.81"/>
  <path class="st3" d="M240.12 417.96v56.37M301.62 417.96v56.37"/>
  <path class="st4" d="M309 450.5h59.7"/>
  <path class="st1" d="M374 450.5l-7.46 3.05 1.77-3.05-1.77-3.05z"/>
  <text transform="translate(320.342 445.96)" class="st1 st2">prepare</text>
  <path class="st4" d="M428.5 450.5h74.7"/>
  <path class="st1" d="M508.5 450.5l-7.46 3.05 1.77-3.05-1.77-3.05z"/>
  <text transform="translate(442.896 444.96)">
    <tspan x="0" y="0" class="st1 st2">generic</tspan>
    <tspan x="-7.5" y="16" class="st1 st2">components</tspan>
  </text>
  <text transform="translate(387.18 435.96)" class="st1 st2">UI-data</text>
  <text transform="translate(564.91 435.96)" class="st1 st2">DOM</text>
  <path class="st4" d="M323.5 394.5L305 409.63"/>
  <path class="st1" d="M301.5 412.5c1.15-1.96 2.34-4.68 2.7-6.82l1.26 3.58 3.26 1.94c-2.17-.07-5.07.56-7.22 1.3z"/>
  <g>
    <path class="st4" d="M404.5 441.5l-14 30M404.02 442.71l20.48 29.79"/>
    <circle class="st1" transform="rotate(-27.365 404.043 442.822)" cx="404.1" cy="442.82" r="2.82"/>
    <path class="st4" d="M390.5 471.5l-14.43 16.56M390.5 471.5l13 15M403.5 486.5l-12 20M403.5 486.5l12 21"/>
  </g>
  <g>
    <path class="st4" d="M575.5 441.5l-14 30M575.02 442.71l20.48 29.79"/>
    <circle class="st1" transform="rotate(-27.365 575.032 442.833)" cx="575.1" cy="442.82" r="2.82"/>
    <path class="st4" d="M561.5 471.5l-14.43 16.56M561.5 471.5l13 15M574.5 486.5l-12 20M574.5 486.5l12 21"/>
  </g>
  <path class="st3" d="M519.5 422.59H626"/>
  <g>
    <path class="st4" d="M555.5 418.5l-28.32-20.36"/>
    <path class="st1" d="M523.5 395.5c2.19.6 5.12 1.05 7.29.85l-3.13 2.14-1.03 3.65c-.5-2.12-1.86-4.76-3.13-6.64z"/>
  </g>
  <text transform="translate(540.81 403.481)" class="st1 st5">Actions as data</text>
  <g>
    <path class="st4" d="M385.5 385.5l63.47.93"/>
    <path class="st1" d="M453.5 386.5c-2.14.76-4.8 2.07-6.46 3.47l1.34-3.55-1.24-3.58c1.62 1.45 4.24 2.84 6.36 3.66z"/>
  </g>
  <text transform="translate(389.202 380.862)" class="st1 st5">watches & executes</text>
</svg>

Observe how all the arrows flow out from `main`, and one way. The event bus is
the mechanism that inverts the dependency so we can communicate actions without
introducing circular dependencies.

PS! [Ove](https://twitter.com/ovegram/status/1222819751279329281) asked a timely
question when this blog post was published. In response, I have written a bit
more about [the interplay between generic UI
components](/interplay-between-generic-ui-components/).

## From Domain Data to Generic Components

Since the components do not speak the domain language, we need an interpreter.
That's the `prepare` function. It takes the domain data from the central data
source and converts it into custom data for precisely the UI we are rendering
now.

The data from `prepare` should, as far as possible, reflect the UI. It builds a
tree structure that can be sent directly down to the components. Note how the
UI-data and DOM trees in the illustration above have the same structure.

This means that the component code itself can be virtually free of logic. UI
code is notoriously difficult to test. With this approach we can test the UI
data instead of the components, giving us testability of the important parts,
while freeing us from the minutia of the component implementation.

## In Conclusion

This is an architecture I have happily used on small and large projects for many
years, but what do we actually get?

- A data flow that is easy to follow.
- Reusable components that implement the design.
- A reproducible user interface due to a single data source.
- Freedom from the perpetual framework race.
