package com.example;

import java.util.Random;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class FlowDemo {

  public static void main(String[] args) throws InterruptedException {
    SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

    Subscriber<Integer> subscriber = new Subscriber<>() {
      private Subscription subscription;

      @Override
      public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1L);
      }

      @Override
      public void onNext(Integer item) {
        System.out.println("receive item: " + item);
        try {
          TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        this.subscription.request(1L);
      }

      @Override
      public void onError(Throwable throwable) {
        throwable.printStackTrace();
      }

      @Override
      public void onComplete() {
        System.out.println("done");
      }
    };

    publisher.subscribe(subscriber);

    new Random().ints().limit(300).forEach(item -> {
      System.out.println("publish item: " + item);
      // 阻塞队列
      publisher.submit(item);
    });

    publisher.close();

    TimeUnit.SECONDS.sleep(1L);
  }
}
