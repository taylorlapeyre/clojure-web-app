![clj](http://i.imgur.com/Zlila8O.png)

# A Clojure Web App

This is a website written entirely in Clojure &amp; Clojurescript. This guide will give you an overview of what exactly that means and how it all fits together.

### Highlights of This Approach

- **Functional programming**
- It's written in **Clojure**, the best language
- Even the JavaScript is written in Clojure
- Speaking of JavaScript, the front end is made with **React**
- Development is *delightful* because of **instantaneous interactive changes**
- **Statefulness** is made obvious and is kept on the edges of the system
- It uses a client-server model with **best practices** everywhere (JSON, REST)

### But it's a pain to set up, right?

Nope!

- You have [Java](http://www.teamten.com/lawrence/writings/java-for-everything.html) installed, right? Right.
- [Leiningen](http://leiningen.org/) is the package and project manager for Clojure. [Download it.](http://leiningen.org/)
``` bash
$ lein cljsbuild once
$ lein ring server
```

What will this do for you?

- Download Clojure if you don't have it already
- Download all of the website's dependencies
- Compile all of your Clojurescript into JavaScript
- Start a server for you and open your browser to the right place.

**Note**: You now no longer need to manually manage your programming language. To Leiningen, Clojure is just another dependency like anything else.

### Writing Clojure

Clojure's development tools make writing code a pleasure. The **REPL** is everything you need.

``` bash
$ lein ring server # in another terminal tab or something
$ lein repl
wishwheel.handler=> (+ 1 2 3)
6
```

The first command (`lein ring server`) simply starts a server on localhost that will serve your Clojure files. The second starts a REPL that you can use to test and write Clojure code interactively. That (and a good editor) are all you need to develop a back end.

But what about the Clojurescript? That's the best part.

### Writing Clojurescript

The website will continuously poll a specific port on your computer, waiting for a connection.

To enable that connection, run the following command in your REPL:
``` clojure
wishwheel.handler=> (start-figwheel)
```

Once connected, your browser will magically receive any changes to your Clojurescript files in real time, without the need for a page reload. Furthermore, source maps will be automatically generated. Any errors you see in the browser console will link directly to Clojurescript.

But that's not all. In the REPL, run the following command:

```
wishwheel.handler=> (browser-repl)
<< started Weasel server on ws://0.0.0.0:9001 >>
Type `:cljs/quit` to stop the ClojureScript REPL
nil
cljs.user=> (.getElementById js/document "app")
#<[object HTMLDivElement]>
```

Your REPL is now essentially your browser's JavaScript console, but in Clojure. You can use it to make changes to the DOM and inspect your code in real time.

### Getting it Ready for Production

Okay, so we have a really great development environment and great tooling. But what about deployment?

Good news! This repository is **100% Heroku-ready**. Just `git push heroku master` and you're set.

Don't have Heroku? No problem. You can compile and compress your entire application down into a single `jar` file with one command:

``` bash
$ lein uberjar
```

Before jar'ing, leiningen will gzip and minify all of your assets. In addition, Clojurescript will be compiled down to the fastest possible JavaScript.

You can run your new `jar` file with java the same way as any other, and can distribute it freely.
