package com.example.framework;

public interface RestHandler {

  void init(ServerInfo serverInfo);

  Object invoke(MethodInfo methodInfo);
}
