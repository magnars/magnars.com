:page/title Interplay Between Generic UI Components
:blog-post/published #time/ldt "2020-10-28T09:00:00"
:blog-post/tags [:frontend :design :functional-programming]
:blog-post/original

{:blog "Kodemaker"
 :url "https://www.kodemaker.no/blogg/2020-10-samspill-mellom-generiske-ui-komponenter/"}

:blog-post/description

If you have a textbox and a button, who is responsible for taking the value from
the textbox and sending it to the event bus when the button is pressed?

:blog-post/body

After my blog post on [a simple frontend architecture that
works](/simple-frontend-architecture/),
[Ove](https://twitter.com/ovegram/status/1222819751279329281) asked: "If you
have a textbox and a button, who is responsible for taking the value from the
textbox and sending it to the event bus when the button is pressed?" It's a
timely question with some interesting details.

Let's start with a little recap: We build our UI with generic components. They
are the implementation of our visual expression, but do not know our domain. They
are unaware of the context in which they are used. *They do not know what
actions are performed when buttons are pressed*.

Thus, they are building blocks used in many contexts. The actions might be
different each time. We send actions into the components in the form of data.
For example:

```js
PrimaryButton({action: ["sign-in"]})
```

Here, a centrally registered event handler will listen for `"sign-in"` on the
bus and carry out the action. But how does it access its data? In this case,
where do the username and password come from?

I wrote in the introduction that it was a timely question. That's because I
haven't found one solution that always works well. What I have found are 1) the
solution that can sometimes be slow, and 2) the solution that places some
limitations on what can be achieved.

## The Solution That Can Sometimes Be Slow

This one likely appears the most correct. In short: Every time an input field
changes value (on change, on blur, on key up?), the updated value is
communicated back to the central data registry.

It's done somewhat like this:

```js
TextInput({onChange: ["save", "sign-in-form/username", "@value"]})
```

Then, it's up to the `TextInput` component to register an appropriate DOM event
handler and fire an event on the bus with `"@value"` replaced by the actual
value.

In the main function, a general `"save"` event handler is implemented that
stores the data and triggers a new prepare followed by an update of the UI.

Naturally, it's this last part that can sometimes be slow. If prepare has a lot
of work to do, it can be excessive to run it for every keystroke.

Some tricks:

- remember that this almost always performs well enough, but if not...
- perhaps updating on `onBlur` instead of `onKeyUp` is sufficient?
- maybe `prepare` can be split into two: one for heavy operations and one for quick ones?
- perhaps a little throttle could be helpful?
- maybe you can use the alternative approach from the next section?

Ove asked: "Who is responsible for taking the value from the textbox and sending
it to the event bus when the button is pressed?"

The answer in this case is: The data is already stored centrally by the time the
button is pressed. The event handler can fetch them directly from the store.

## The Solution That Imposes Some Limitations on What Can Be Achieved

I find this solution a bit messy, but sometimes necessary. In short: The event
handler itself retrieves values from the DOM.

To make this work, we send an ID to the input fields:

```js
TextInput({id: "sign-in-form/username"})
```

The only job of `TextInput` is to set this ID in the DOM. When the event handler
later deals with the `"sign-in"` event, it goes into the DOM itself and
retrieves values from the fields.

This is obviously a faster solution, precisely because prepare is not run anew
and the UI does not update when values change. In other words: the values in the
fields are invisible to the prepare function and thus cannot be used. The good
old two-way data binding party trick cannot be performed. Or, perhaps more
crucially: we cannot provide validation errors along the way.

Here too, there are some tricks:

- maybe you can actually use the solution from the previous section?
- maybe it's enough to show validation errors when the button is pressed?
- or how about a hybrid model where only the most important fields are kept in sync?

The answer to Ove's question in this case is: It's the event handler's
responsibility to tie the loose ends together.

## In Conclusion

I sometimes hear objections to [this frontend
architecture](/simple-frontend-architecture/). That it cannot work in
practice. That running the prepare function so often becomes too slow. It also
surprises me, at times, how fast our computers and phones have become. They have
no problem chewing through some data.

Thanks to [Ove](https://twitter.com/ovegram/status/1222819751279329281) for a
great question!
