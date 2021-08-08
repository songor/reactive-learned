package com.example.webflux.client;

import com.example.framework.JdkProxyCreatorImpl;
import com.example.framework.ProxyCreator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebfluxClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebfluxClientApplication.class, args);
  }

  @Bean
  public ProxyCreator jdkProxyCreator() {
    return new JdkProxyCreatorImpl();
  }

  @Bean
  public FactoryBean<IUserAPIServer> userAPIServer(ProxyCreator proxyCreator) {
    return new FactoryBean<IUserAPIServer>() {
      @Override
      public IUserAPIServer getObject() {
        return (IUserAPIServer) proxyCreator.createProxy(IUserAPIServer.class);
      }

      @Override
      public Class<?> getObjectType() {
        return IUserAPIServer.class;
      }
    };
  }
}
