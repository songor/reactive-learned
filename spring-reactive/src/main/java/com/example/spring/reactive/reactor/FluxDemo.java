package com.example.spring.reactive.reactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class FluxDemo {

  public static void main(String[] args) {
    Flux.just("A", "B", "C").subscribe(FluxDemo::println);

    // 切换线程池
    Flux.just("A", "B", "C")
        .publishOn(Schedulers.elastic())
        .map(String::toLowerCase)
        .subscribe(FluxDemo::println, FluxDemo::println, () -> FluxDemo.println("complete"),
            subscription -> subscription.request(1L));

    Flux.just("A", "B", "C")
        .publishOn(Schedulers.elastic())
        .map(String::toLowerCase)
        .subscribe(new Subscriber<String>() {
          private Subscription s;

          @Override
          public void onSubscribe(Subscription subscription) {
            this.s = subscription;
            this.s.request(1L);
          }

          @Override
          public void onNext(String str) {
            println(str);
            this.s.request(1L);
          }

          @Override
          public void onError(Throwable throwable) {
            println(throwable.getMessage());
          }

          @Override
          public void onComplete() {
            println("complete");
          }
        });

    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static void println(Object obj) {
    System.out.println("[" + Thread.currentThread().getName() + "] " + obj);
  }
}
