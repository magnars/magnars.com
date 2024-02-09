:page/title Static sites: server-even-less
:blog-post/published #time/ldt "2023-10-18T15:30:00"
:blog-post/tags [:statisk-html]
:open-graph/description

You're going to have a hard time finding something simpler and snappier than
serving static HTML and CSS to people. I'm writing about a grossly underrated
technique for creating websites.

:blog-post/description

You're going to have a hard time finding something simpler and snappier than
serving static HTML and CSS to people. Heard of "serverless"? Nope, this is
server nada! I'm writing about a grossly underrated technique for creating
websites.

:blog-post/body

When I was about to create [kodemaker.no](https://www.kodemaker.no) ten years
ago, I had just read and reread the book [High Performance Web
Sites](https://biblio.co.uk/book/high-performance-web-sites-essential-knowledge/d/920516130?aid=frg&gclid=CjwKCAjwvfmoBhAwEiwAG2tqzJsOBzLsRGv24PNBTUuQZphsofoPkp-Fn4q0SWBPYuwRg-hMbYCfABoC7eoQAvD_BwE)
by [Steve Souders](https://stevesouders.com). I loved that book! Fast websites
were definitely my thing. Steve and I were best friends -- at least in my head.
I remember standing on the train platform. Ten minutes until the train arrived?
Down into the bag and up with the book to read a bit more (books were made from
dead trees back then).

Anyway, it had to be fast, I was sure of that. Therefore, I was determined that
the new pages should be pre-rendered. All content in flat files, served straight
from the disk. No running server. You save countless milliseconds like that.

I used the Christmas holiday to create
[Stasis](https://github.com/magnars/stasis), a toolkit for building static
websites, so that work in January could start at a full sprint. January was a
blast. It turned out as great as I had dreamed. On the production server, all
HTML and CSS were pre-rendered on disk, served by NGINX, with Varnish in front.

Thanks to Varnish, the files were even served *straight from memory.*

So yes, the websites were lightning-fast, but something else was fast too. Much
faster, actually, than I had anticipated.

## A Simpler Model

Have you ever considered how much time and mental capacity is consumed because
we need to deal with a running server process?

Neither had I.

At least not until this project.

Beyond compilation and build steps, the running process has its own runtime,
with its own dependencies, its own state, its own bugs. There are asynchronous
calls, from frontend to frontend-for-backend to backend to database. It needs to
be operated and maintained, monitored, and logged. Exceptions and downtime.
Model, View, Controller. It's a hassle.

Okay, it's often necessary... a necessary hassle. But what about the times we
don't *need* this running process? Many websites just need to present
information. Suddenly, the solution can be dramatically simpler.

That's what surprised me back then, ten years ago. Without a server running,
there were so many considerations I could ditch, like a burden I no longer
needed to bear. My job became much simpler. I crunched some data and spat some
HTML out onto a disk. As a build step. Done.

No wonder it was easy to create.

## Server Nope

Or was it server nada?

Regardless, the model to get this online became incredibly simple. Back in the
day, I used nginx and a filesystem. More recently, even better: We store all the
files in a bucket on S3 / Google Cloud Storage and point the load balancer
directly at it.

It's so stress-free that you can't help but laugh.

This is the true serverless.
