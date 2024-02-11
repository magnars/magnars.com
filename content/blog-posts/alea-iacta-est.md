:page/title The Die is Cast with CSS
:blog-post/published #time/ldt "2019-09-04T09:00:00"
:blog-post/tags [:css :html :frontend]
:blog-post/original

{:blog "Kodemaker"
 :url "https://www.kodemaker.no/blogg/2019-09-terningene-er-kastet/"}

:blog-post/description

Dice games are far more enjoyable if you can see the dice roll, so I rolled up
my sleeves and wrote some CSS in preparation for the presentation. Here's what I
learned about rolling dice with CSS.

:blog-post/body

<style>
.example {
    border: 1px solid #222;
    padding: 60px;
    margin: 20px 0;
    text-align: center;
    position: relative;
}
.example-label {
    position: absolute;
    top: 10px;
    left: 20px;
    color: #777;
    font-family: monospace;
    text-align: left;
}
.persp {
    perspective: 400px;
}
.persp-orig {
    perspective-origin: 50% 0%;
}
.dice,
.cube {
    width: 120px;
    height: 120px;
    position: relative;
    transform-style: preserve-3d;
    display: inline-block;
    animation-timing-function: ease-in-out;
    animation-iteration-count: 1;
    animation-duration: 1.4s;
    animation-fill-mode: both;
}
.cube.w-transition {
    transition: transform 600ms ease;
}
.cube.rotated {
    transform: rotateY(30deg) rotateX(30deg) rotateZ(30deg);
}
.face {
    height: 120px;
    width: 120px;
    background-color: rgba(0,0,0,0.7);
    position: absolute;
    border-radius: 6px;
    border: 1px solid #00f;
    box-shadow: inset 0 0 20px rgba(0,0,255,0.3);
    font-size: 60px;
    line-height: 120px;
    color: #669;
}
.fail-face-1 { }
.fail-face-2 { transform: rotateY(90deg); }
.fail-face-3 { transform: rotateY(90deg) rotateX(90deg); }
.fail-face-4 { transform: rotateY(180deg) rotateZ(90deg); }
.fail-face-5 { transform: rotateY(-90deg) rotateZ(90deg); }
.fail-face-6 { transform: rotateX(-90deg); }

.face-1 { transform: translateZ(60px); }
.face-2 { transform: rotateY(90deg) translateZ(60px); }
.face-3 { transform: rotateY(90deg) rotateX(90deg) translateZ(60px); }
.face-4 { transform: rotateY(180deg) rotateZ(90deg) translateZ(60px); }
.face-5 { transform: rotateY(-90deg) rotateZ(90deg) translateZ(60px); }
.face-6 { transform: rotateX(-90deg) translateZ(60px); }

.facing-1 { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
.facing-2 { transform: rotateX(0deg) rotateY(-90deg) rotateZ(0deg); }
.facing-3 { transform: rotateX(-90deg) rotateY(-90deg) rotateZ(0deg); }
.facing-4 { transform: rotateX(0deg) rotateY(180deg) rotateZ(90deg); }
.facing-5 { transform: rotateX(270deg) rotateY(180deg) rotateZ(90deg); }
.facing-6 { transform: rotateX(90deg) rotateY(0deg) rotateZ(0deg); }

@keyframes roll-1-to-1 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-1-to-2 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
}

@keyframes roll-1-to-3 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-1-to-4 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-1-to-5 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-1-to-6 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
}

.roll-1-to-1 { animation-name: roll-1-to-1; }
.roll-1-to-2 { animation-name: roll-1-to-2; }
.roll-1-to-3 { animation-name: roll-1-to-3; }
.roll-1-to-4 { animation-name: roll-1-to-4; }
.roll-1-to-5 { animation-name: roll-1-to-5; }
.roll-1-to-6 { animation-name: roll-1-to-6; }

@keyframes roll-2-to-1 {
    from { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-2-to-2 {
    from { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
    to { transform: rotateX(0deg) rotateY(-810deg) rotateZ(360deg); }
}

@keyframes roll-2-to-3 {
    from { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
    to { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-2-to-4 {
    from { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
    to { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-2-to-5 {
    from { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
    to { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-2-to-6 {
    from { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
}

.roll-2-to-1 { animation-name: roll-2-to-1; }
.roll-2-to-2 { animation-name: roll-2-to-2; }
.roll-2-to-3 { animation-name: roll-2-to-3; }
.roll-2-to-4 { animation-name: roll-2-to-4; }
.roll-2-to-5 { animation-name: roll-2-to-5; }
.roll-2-to-6 { animation-name: roll-2-to-6; }

@keyframes roll-3-to-1 {
    from { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-3-to-2 {
    from { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
    to { transform: rotateX(0deg) rotateY(-810deg) rotateZ(360deg); }
}

@keyframes roll-3-to-3 {
    from { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
    to { transform: rotateX(270deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-3-to-4 {
    from { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
    to { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-3-to-5 {
    from { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
    to { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-3-to-6 {
    from { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
}

.roll-3-to-1 { animation-name: roll-3-to-1; }
.roll-3-to-2 { animation-name: roll-3-to-2; }
.roll-3-to-3 { animation-name: roll-3-to-3; }
.roll-3-to-4 { animation-name: roll-3-to-4; }
.roll-3-to-5 { animation-name: roll-3-to-5; }
.roll-3-to-6 { animation-name: roll-3-to-6; }

@keyframes roll-4-to-1 {
    from { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-4-to-2 {
    from { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
    to { transform: rotateX(0deg) rotateY(-810deg) rotateZ(360deg); }
}

@keyframes roll-4-to-3 {
    from { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
    to { transform: rotateX(270deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-4-to-4 {
    from { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
    to { transform: rotateX(360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-4-to-5 {
    from { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
    to { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-4-to-6 {
    from { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
}

.roll-4-to-1 { animation-name: roll-4-to-1; }
.roll-4-to-2 { animation-name: roll-4-to-2; }
.roll-4-to-3 { animation-name: roll-4-to-3; }
.roll-4-to-4 { animation-name: roll-4-to-4; }
.roll-4-to-5 { animation-name: roll-4-to-5; }
.roll-4-to-6 { animation-name: roll-4-to-6; }

@keyframes roll-5-to-1 {
    from { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-5-to-2 {
    from { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
    to { transform: rotateX(0deg) rotateY(-810deg) rotateZ(360deg); }
}

@keyframes roll-5-to-3 {
    from { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
    to { transform: rotateX(270deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-5-to-4 {
    from { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
    to { transform: rotateX(360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-5-to-5 {
    from { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
    to { transform: rotateX(270deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-5-to-6 {
    from { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
}

.roll-5-to-1 { animation-name: roll-5-to-1; }
.roll-5-to-2 { animation-name: roll-5-to-2; }
.roll-5-to-3 { animation-name: roll-5-to-3; }
.roll-5-to-4 { animation-name: roll-5-to-4; }
.roll-5-to-5 { animation-name: roll-5-to-5; }
.roll-5-to-6 { animation-name: roll-5-to-6; }

@keyframes roll-6-to-1 {
    from { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-6-to-2 {
    from { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
    to { transform: rotateX(0deg) rotateY(-810deg) rotateZ(360deg); }
}

@keyframes roll-6-to-3 {
    from { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
    to { transform: rotateX(270deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-6-to-4 {
    from { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
    to { transform: rotateX(360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-6-to-5 {
    from { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
    to { transform: rotateX(270deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-6-to-6 {
    from { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(720deg); }
}

.roll-6-to-1 { animation-name: roll-6-to-1; }
.roll-6-to-2 { animation-name: roll-6-to-2; }
.roll-6-to-3 { animation-name: roll-6-to-3; }
.roll-6-to-4 { animation-name: roll-6-to-4; }
.roll-6-to-5 { animation-name: roll-6-to-5; }
.roll-6-to-6 { animation-name: roll-6-to-6; }

@keyframes scale {
    from { transform: scale3d(1, 1, 1) translate3d(0, 0, 0); }
    50% { transform: scale3d(0.2, 0.2, 0.2) translate3d(0, -200px, 0); }
    to { transform: scale3d(1, 1, 1) translate3d(0, 0, 0); }
}

.rolling.dice {
    animation-name: scale;
    animation-timing-function: ease-in-out;
    animation-iteration-count: 1;
    animation-duration: 1.8s;
    transition: transform 300ms ease;
}
</style>

[Christian](https://cjohansen.no) and I will be making a dice game in season 2
of [Parens of the Dead](https://www.parens-of-the-dead.com). Dice games are far
more enjoyable if you can see the dice roll, so I rolled up my sleeves and wrote
some CSS in preparation for the presentation. Here's what I learned about
rolling dice with CSS.

## First, We Need a Cube

A cube has six sides, and we need to draw each one of them:

```html
<div class="example">
  <div class="cube">
    <div class="face face-1"></div>
    <div class="face face-2"></div>
    <div class="face face-3"></div>
    <div class="face face-4"></div>
    <div class="face face-5"></div>
    <div class="face face-6"></div>
  </div>
</div>
```

Let's anchor the sides in a cube:

```css
.cube {
    width: 120px;
    height: 120px;
    position: relative;
    transform-style: preserve-3d;
    display: inline-block;
}
```

Here we use `position: relative` because all the sides of the dice should
initially overlay each other.

The next important point is `preserve-3d`: This allows us to rotate this element
and its children in the same three-dimensional context. We'll get back to this
soon.

Time to add some styling to the sides:

```css
.face {
    height: 120px;
    width: 120px;
    background-color: rgba(255,255,255,0.7);
    position: absolute;
    border-radius: 6px;
    border: 1px solid #aaa;
    box-shadow: inset 0 0 20px rgba(0,0,0,0.2);
}
```

At this point, our dice looks like this:

<div class="example">
<div class="cube">
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
</div>
</div>

Not so three-dimensional yet. It's a bit hard to see that there are six sides.
To be able to see what happens next, we first need to rotate the cube a bit:

```css
.cube {
    transform: rotateY(30deg) rotateX(30deg) rotateZ(30deg);
}
```

<div class="example">
<div class="cube rotated">
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
</div>
</div>

Let's start a bit naively and just rotate all the sides into place:

```css
.face-1 { }
.face-2 { transform: rotateY(90deg); }
.face-3 { transform: rotateY(90deg) rotateX(90deg); }
.face-4 { transform: rotateY(180deg) rotateZ(90deg); }
.face-5 { transform: rotateY(-90deg) rotateZ(90deg); }
.face-6 { transform: rotateX(-90deg); }
```

<div class="example">
<div class="cube rotated">
  <div class="face fail-face-1"></div>
  <div class="face fail-face-2"></div>
  <div class="face fail-face-3"></div>
  <div class="face fail-face-4"></div>
  <div class="face fail-face-5"></div>
  <div class="face fail-face-6"></div>
</div>
</div>

Observe that all the sides have rotated around the cube's central axis. It
doesn't look like any dice just yet.

We need to push each side away from the center. By how much? Since our sides are
`120px` big, we need to push `60px` in each direction. Voila:

```css
.face-1 { transform: translateZ(60px); }
.face-2 { transform: rotateY(90deg) translateZ(60px); }
.face-3 { transform: rotateY(90deg) rotateX(90deg) translateZ(60px); }
.face-4 { transform: rotateY(180deg) rotateZ(90deg) translateZ(60px); }
.face-5 { transform: rotateY(-90deg) rotateZ(90deg) translateZ(60px); }
.face-6 { transform: rotateX(-90deg) translateZ(60px); }
```

<div class="example">
<div class="cube rotated">
  <div class="face face-1"></div>
  <div class="face face-2"></div>
  <div class="face face-3"></div>
  <div class="face face-4"></div>
  <div class="face face-5"></div>
  <div class="face face-6"></div>
</div>
</div>

And thus, we have a cube. But it doesn't look quite... right. There's something
wrong with the perspective:

```css
.example {
    perspective: 400px;
}
```

<div class="example persp">
<div class="cube rotated">
  <div class="face face-1"></div>
  <div class="face face-2"></div>
  <div class="face face-3"></div>
  <div class="face face-4"></div>
  <div class="face face-5"></div>
  <div class="face face-6"></div>
</div>
</div>

Ahh, that's better.

When setting `perspective`, you're defining how far away the Z0 position points
are from the user. Points with a higher Z feel closer, and points with a lower Z
feel further away. The vanishing point is by default in the middle of the
element with perspective, but this can also be moved.

Let's try that. First, we remove the rotation of the cube to make it clearer:

<div class="example persp">
<div class="cube">
  <div class="face face-1"></div>
  <div class="face face-2"></div>
  <div class="face face-3"></div>
  <div class="face face-4"></div>
  <div class="face face-5"></div>
  <div class="face face-6"></div>
</div>
</div>

Now we can move the vanishing point:

```css
.example {
    perspective-origin: 50% 0%;
}
```

<div class="example persp persp-orig">
<div class="cube">
  <div class="face face-1"></div>
  <div class="face face-2"></div>
  <div class="face face-3"></div>
  <div class="face face-4"></div>
  <div class="face face-5"></div>
  <div class="face face-6"></div>
</div>
</div>

Perfect. Now it looks more like the dice is lying on a table, rather than
floating in the air.

PS! Earlier, I briefly mentioned `transform-style: preserve-3d;`. Now it's
easier to explain why this is important: We rotate the cube and each side with
separate `transform` rules. Without specifying `preserve-3d`, these would have
been rotated independently of each other. Now they are rotated in the same
context.

Let's also slap some numbers on the sides:

```html
<div class="example">
  <div class="cube">
    <div class="face face-1">1</div>
    <div class="face face-2">2</div>
    <div class="face face-3">3</div>
    <div class="face face-4">4</div>
    <div class="face face-5">5</div>
    <div class="face face-6">6</div>
  </div>
</div>
```

```css
.face {
    font-size: 60px;
    line-height: 120px;
    color: #aaa;
}
```
<div class="example persp persp-orig">
<div class="cube">
  <div class="face face-1">1</div>
  <div class="face face-2">2</div>
  <div class="face face-3">3</div>
  <div class="face face-4">4</div>
  <div class="face face-5">5</div>
  <div class="face face-6">6</div>
</div>
</div>

## Then, The Throwing

For a dice to be perceived as thrown, it must:

- be in the air
- rotate
- land on a side

Let's start from the beginning. To give the impression of being in the air
without taking up too much screen space, I decided to zoom it away and a bit up.
Here's the animation definition:

```css
@keyframes scale {
    from { transform: scale3d(1, 1, 1) translate3d(0, 0, 0); }
    50% { transform: scale3d(0.2, 0.2, 0.2) translate3d(0, -200px, 0); }
    to { transform: scale3d(1, 1, 1) translate3d(0, 0, 0); }
}
```

It starts in the original position, disappears down to 20% in size and up 200px,
before it comes back again.

Since I want this animation to happen independently of how the dice is rotated,
I have to do the scaling outside of `.cube`. I create a `.dice`:

```html
<div class="example">
  <div class="dice">
    <div class="cube">
      <div class="face face-1">1</div>
      <div class="face face-2">2</div>
      <div class="face face-3">3</div>
      <div class="face face-4">4</div>
      <div class="face face-5">5</div>
      <div class="face face-6">6</div>
    </div>
  </div>
</div>
```

It gets the same rules as the cube, like this:

```css
.dice,
.cube {
    width: 120px;
    height: 120px;
    position: relative;
    transform-style: preserve-3d;
    display: inline-block;
}
```

Then we need to enable the animation when the dice is rolled:

```css
.rolling.dice {
    animation-name: scale;
    animation-timing-function: ease-in-out;
    animation-iteration-count: 1;
    animation-duration: 1.8s;
}
```

<script>
    function scaleDice(e) {
       var label = document.getElementById('scaling-class');
       e.className = 'dice rolling';
       label.innerText = '.dice.rolling';
       setTimeout(function () { e.className = 'dice'; label.innerText = '.dice'; }, 2000);
    }
</script>

<div class="example persp persp-orig">
  <div class="example-label" id="scaling-class">.dice</div>
  <div class="dice" onclick="scaleDice(this)">
    <div class="cube">
      <div class="face face-1">1</div>
      <div class="face face-2">2</div>
      <div class="face face-3">3</div>
      <div class="face face-4">4</div>
      <div class="face face-5">5</div>
      <div class="face face-6">6</div>
    </div>
  </div>
</div>

Click on the dice to "roll" it. The script just adds the class `rolling` -
that's all it takes to kick the animation into gear. (the class is also removed
again after a couple of seconds, so you can click multiple times)

Time to rotate the dice as well. We can start by finding out how the cube needs
to be rotated to show each side:

```css
.facing-1 { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
.facing-2 { transform: rotateX(0deg) rotateY(-90deg) rotateZ(0deg); }
.facing-3 { transform: rotateX(-90deg) rotateY(-90deg) rotateZ(0deg); }
.facing-4 { transform: rotateX(-90deg) rotateY(180deg) rotateZ(90deg); }
.facing-5 { transform: rotateX(90deg) rotateY(180deg) rotateZ(90deg); }
.facing-6 { transform: rotateX(90deg) rotateY(0deg) rotateZ(0deg); }
```

We can also add a transition, for the sake of appearance:

```css
.cube {
    transition: transform 600ms ease;
}
```

<script>
    function changeFacing(e) {
        var i = Number(e.className.substring(25));
        var f = ((i % 6) + 1);
        e.className = 'cube w-transition facing-' + f;
        document.getElementById("facing-class").innerText = ".cube.facing-" + f;
    }
</script>
<div class="example persp persp-orig">
  <div class="example-label" id="facing-class">.cube.facing-1</div>
  <div class="dice">
    <div class="cube w-transition facing-1" onclick="changeFacing(this)">
      <div class="face face-1">1</div>
      <div class="face face-2">2</div>
      <div class="face face-3">3</div>
      <div class="face face-4">4</div>
      <div class="face face-5">5</div>
      <div class="face face-6">6</div>
    </div>
  </div>
</div>

Click on the dice to flip it to the next side.

The problem with this technique is that it doesn't quite capture the experience
of a spinning dice in the air. Especially the first transitions were quite dull.
But you might have noticed that the subsequent transitions had more oomph?

The trick here is to rotate to the correct side but spin a few extra times for
good measure. Let's say we're spinning to side 1. Instead of going to `0 0 0`,
we can go to `720 -360 360`. It will be the same side shown, but the cube has to
rotate much more to get there.

It might also happen that the dice lands on the same side it started. Then we
must also ensure that the dice appears to spin a bit first.

What I ended up with was defining animations from/to all sides. Something like
this:

```css
@keyframes roll-1-to-1 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-1-to-2 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
}

@keyframes roll-1-to-3 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-1-to-4 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-1-to-5 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-1-to-6 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
}
```

And so on for `2-to-1`, `2-to-2`, `2-to-3`, etc., etc. A total of 36 keyframes,
with corresponding CSS classes:

```css
.roll-1-to-1 { animation-name: roll-1-to-1; }
.roll-1-to-2 { animation-name: roll-1-to-2; }
.roll-1-to-3 { animation-name: roll-1-to-3; }
.roll-1-to-4 { animation-name: roll-1-to-4; }
.roll-1-to-5 { animation-name: roll-1-to-5; }
.roll-1-to-6 { animation-name: roll-1-to-6; }
```

Here's the animation declaration for the cube:

```css
.cube {
    animation-timing-function: ease-in-out;
    animation-iteration-count: 1;
    animation-duration: 1.4s;
    animation-fill-mode: both;
}
```

And here you can see my last two tricks:

- `animation-fill-mode: both`

    This ensures that the dice retains its position when the animation is
    finished. Without this, the dice would jump back to its original position
    when the animation ends.

- `animation-duration: 1.4s`

    The rotation is set to 1.4s, while the scaling is set to 1.8s (mentioned
    earlier in the article). Thus, the cube will not rotate all the way - it
    stabilizes towards the end, appearing to be gently placed down on the table
    in the last 400ms.

You can see the result here:

<script>
    function rollDice(e) {
        var prev = Number(e.className.substring(e.className.length - 1));
        var next = Math.floor(Math.random()*6) + 1;
        var c = 'roll-' + prev + '-to-' + next;
        var label = document.getElementById("rolling-class");
        e.className = 'cube w-transition ' + c;
        label.innerHTML = '.dice.rolling<br>.cube.' + c;
        var d = document.getElementById("rolling-dice");
        d.className = 'dice rolling';
        setTimeout(function () { d.className = 'dice'; label.innerHTML = '.dice<br>.cube.' + c; }, 2000);
    }
</script>
<div class="example persp persp-orig">
  <div class="example-label" id="rolling-class">.dice<br>.cube.facing-1</div>
  <div class="dice" id="rolling-dice">
    <div class="cube w-transition facing-1" onclick="rollDice(this)">
      <div class="face face-1">1</div>
      <div class="face face-2">2</div>
      <div class="face face-3">3</div>
      <div class="face face-4">4</div>
      <div class="face face-5">5</div>
      <div class="face face-6">6</div>
    </div>
  </div>
</div>

Click to roll the dice.
