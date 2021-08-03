package com.example.servlet;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SyncServlet", value = "/SyncServlet")
public class SyncServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    LocalTime start = LocalTime.now();

    try {
      System.out.println(Thread.currentThread().getName() + " is running");
      TimeUnit.SECONDS.sleep(2L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    LocalTime end = LocalTime.now();
    System.out.println(
        Thread.currentThread().getName() + " - " + Duration.between(start, end).toMillis() + "ms");
    response.getWriter().append("done");
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
  }
}
