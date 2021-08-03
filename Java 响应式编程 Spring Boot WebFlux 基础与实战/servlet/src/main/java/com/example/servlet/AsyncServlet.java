package com.example.servlet;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AsyncServlet", value = "/AsyncServlet", asyncSupported = true)
public class AsyncServlet extends HttpServlet {

  private final ExecutorService executorService = Executors.newFixedThreadPool(5);

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    LocalTime start = LocalTime.now();
    AsyncContext context = request.startAsync();

    executorService.submit(() -> {
      try {
        System.out.println(Thread.currentThread().getName() + " is running");
        TimeUnit.SECONDS.sleep(2L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    LocalTime end = LocalTime.now();
    System.out.println(
        Thread.currentThread().getName() + " - " + Duration.between(start, end).toMillis() + "ms");

    context.getResponse().getWriter().append("done");
    context.complete();
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
  }

  @Override
  public void destroy() {
    executorService.shutdown();
  }
}
