# Spring Boot 2.0 深度实践

### 从 Reactive 到 WebFlux

[Blocking Can Be Wasteful](https://projectreactor.io/docs/core/release/reference/#_blocking_can_be_wasteful)

[Asynchronicity to the Rescue?](https://projectreactor.io/docs/core/release/reference/#_asynchronicity_to_the_rescue)

**Reactive Programming 定义**

[The Reactive Manifesto](https://www.reactivemanifesto.org/)

We want systems that are Responsive, Resilient, Elastic and Message Driven. We call these Reactive Systems.

[Wiki](https://en.wikipedia.org/wiki/Reactive_programming)

In computing, reactive programming is a declarative programming paradigm concerned with data streams and the propagation of change.

[Spring Framework](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-why-reactive)

The term, “reactive,” refers to programming models that are built around reacting to change — network components reacting to I/O events, UI controllers reacting to mouse events, and others. In that sense, non-blocking is reactive, because, instead of being blocked, we are now in the mode of reacting to notifications as operations complete or data becomes available.

[Reactive X](http://reactivex.io/intro.html)

It extends the observer pattern to support sequences of data and/or events and adds operators that allow you to compose sequences together declaratively while abstracting away concerns about things like low-level threading, synchronization, thread-safety, concurrent data structures, and non-blocking I/O.

[Reactor](https://projectreactor.io/docs/core/release/reference/#intro-reactive)

The reactive programming paradigm is often presented in object-oriented languages as an extension of the Observer design pattern. You can also compare the main reactive streams pattern with the familiar Iterator design pattern, as there is a duality to the Iterable-Iterator pair in all of these libraries. One major difference is that, while an Iterator is pull-based, reactive streams are push-based.

["What is Reactive Programming?"](https://gist.github.com/staltz/868e7e9bc2a7b8c1f754#what-is-reactive-programming)

Reactive programming is programming with asynchronous data streams.

In a way, this isn't anything new. Event buses or your typical click events are really an asynchronous event stream, on which you can observe and do some side effects. Reactive is that idea on steroids. You are able to create data streams of anything, not just from click and hover events. Streams are cheap and ubiquitous, anything can be a stream: variables, user inputs, properties, caches, data structures, etc.

**Reactive Programming 使用场景**

[Reactive Streams JVM](https://github.com/reactive-streams/reactive-streams-jvm)

The main goal of Reactive Streams is to govern the exchange of stream data across an asynchronous boundary.

[Spring Framework](https://docs.spring.io/spring-framework/docs/5.0.7.RELEASE/spring-framework-reference/web-reactive.html#webflux-performance)

Performance has many characteristics and meanings. Reactive and non-blocking generally do not make applications run faster. They can, in some cases, for example if using the `WebClient` to execute remote calls in parallel. On the whole it requires more work to do things the non-blocking way and that can increase slightly the required processing time.

The key expected benefit of reactive and non-blocking is the ability to scale with a small, fixed number of threads and less memory. That makes applications more resilient under load because they scale in a more predictable way.

[ReactiveX](http://reactivex.io/intro.html)

The ReactiveX Observable model allows you to treat streams of asynchronous events with the same sort of simple, composable operations that you use for collections of data items like arrays. It frees you from tangled webs of callbacks, and thereby makes your code more readable and less prone to bugs.

[Reactor](https://projectreactor.io/docs/core/release/reference/#_from_imperative_to_reactive_programming)

Reactive libraries, such as Reactor, aim to address these drawbacks of “classic” asynchronous approaches on the JVM while also focusing on a few additional aspects:

* Composability and readability
* Data as a flow manipulated with a rich vocabulary of operators
* Nothing happens until you subscribe
* Backpressure or the ability for the consumer to signal the producer that the rate of emission is too high
* High level but high value abstraction that is concurrency-agnostic

**Reactive Streams 规范**

[Reactive Streams JVM](https://github.com/reactive-streams/reactive-streams-jvm)

In summary, Reactive Streams is a standard and specification for Stream-oriented libraries for the JVM that

* process a potentially unbounded number of elements,
* in sequence,
* asynchronously passing elements between components,
* with mandatory non-blocking backpressure.

The API consists of the following components that are required to be provided by Reactive Stream implementations:

Publisher

Subscriber

Subscription

Processor

**Backpressure**

[Reactive Streams JVM](https://github.com/reactive-streams/reactive-streams-jvm#subscriber-controlled-queue-bounds)

Backpressure is an integral part of this model in order to allow the queues which mediate between threads to be bounded.

Since back-pressure is mandatory the use of unbounded buffers can be avoided. In general, the only time when a queue might grow without bounds is when the publisher side maintains a higher rate than the subscriber for an extended period of time, but this scenario is handled by backpressure instead.

[Reactor](https://projectreactor.io/docs/core/release/reference/#reactive.backpressure)

Propagating signals upstream is also used to implement backpressure, which we described in the assembly line analogy as a feedback signal sent up the line when a workstation processes more slowly than an upstream workstation.

The real mechanism defined by the Reactive Streams specification is pretty close to the analogy: A subscriber can work in unbounded mode and let the source push all the data at its fastest achievable rate or it can use the request mechanism to signal the source that it is ready to process at most n elements.

**Reactive Spring Web**

[Motivation](https://docs.spring.io/spring-framework/docs/5.0.7.RELEASE/spring-framework-reference/web-reactive.html#webflux-new-framework)

Part of the answer is the need for a non-blocking web stack to handle concurrency with a small number of threads and scale with less hardware resources.

The other part of the answer is functional programming.

[Applicability](https://docs.spring.io/spring-framework/docs/5.0.7.RELEASE/spring-framework-reference/web-reactive.html#webflux-framework-choice)

- If you have a Spring MVC application that works fine, there is no need to change. Imperative programming is the easiest way to write, understand, and debug code. You have maximum choice of libraries since historically most are blocking.

- A simple way to evaluate an application is to check its dependencies. If you have blocking persistence APIs (JPA, JDBC), or networking APIs to use, then Spring MVC is the best choice for common architectures at least. It is technically feasible with both Reactor and RxJava to perform blocking calls on a separate thread but you wouldn’t be making the most of a non-blocking web stack.

- If you have a Spring MVC application with calls to remote services, try the reactive `WebClient`. You can return reactive types (Reactor, RxJava, [or other](https://docs.spring.io/spring-framework/docs/5.0.7.RELEASE/spring-framework-reference/web-reactive.html#webflux-reactive-libraries)) directly from Spring MVC controller methods. The greater the latency per call, or the interdependency among calls, the more dramatic the benefits. Spring MVC controllers can call other reactive components too.

[Concurrency Model](https://docs.spring.io/spring-framework/docs/5.0.7.RELEASE/spring-framework-reference/web-reactive.html#webflux-concurrency-model)

In Spring MVC, and servlet applications in general, it is assumed that applications *may block* the current thread, e.g. for remote calls, and for this reason servlet containers use a large thread pool, to absorb potential blocking during request handling.

In Spring WebFlux, and non-blocking servers in general, it is assumed that applications *will not block*, and therefore non-blocking servers use a small, fixed-size thread pool (event loop workers) to handle requests.

### WebFlux 核心

[Spring 5 WebFlux: Performance tests](https://blog.ippon.tech/spring-5-webflux-performance-tests/)

No improvement in speed was observed with our reactive apps (the Gatling results are even slightly worse).
