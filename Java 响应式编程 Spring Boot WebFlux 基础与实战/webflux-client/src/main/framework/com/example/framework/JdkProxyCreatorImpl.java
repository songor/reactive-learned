package com.example.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class JdkProxyCreatorImpl implements ProxyCreator {

  @Override
  public Object createProxy(Class<?> type) {
    ServerInfo serverInfo = getServerInfo(type);

    RestHandler handler = new WebClientRestHandlerImpl();

    handler.init(serverInfo);

    return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{type},
        (proxy, method, args) -> {
          MethodInfo methodInfo = getMethodInfo(method, args);
          return handler.invoke(methodInfo);
        });
  }

  private ServerInfo getServerInfo(Class<?> type) {
    ApiServer annotation = type.getAnnotation(ApiServer.class);
    String url = annotation.value();
    return new ServerInfo(url);
  }

  private MethodInfo getMethodInfo(Method method, Object[] args) {
    MethodInfo methodInfo = new MethodInfo();

    extractUriAndMethod(method, methodInfo);

    extractParamAndBody(method, args, methodInfo);

    extractReturnInfo(method, methodInfo);

    return methodInfo;
  }

  private void extractUriAndMethod(Method method, MethodInfo methodInfo) {
    Annotation[] annotations = method.getAnnotations();
    for (Annotation annotation : annotations) {
      if (annotation instanceof GetMapping) {
        GetMapping getMappingAnnotation = (GetMapping) annotation;
        methodInfo.setUri(getMappingAnnotation.value()[0]);
        methodInfo.setMethod(HttpMethod.GET);
      }

      if (annotation instanceof PostMapping) {
        PostMapping postMappingAnnotation = (PostMapping) annotation;
        methodInfo.setUri(postMappingAnnotation.value()[0]);
        methodInfo.setMethod(HttpMethod.POST);
      }

      if (annotation instanceof DeleteMapping) {
        DeleteMapping deleteMappingAnnotation = (DeleteMapping) annotation;
        methodInfo.setUri(deleteMappingAnnotation.value()[0]);
        methodInfo.setMethod(HttpMethod.DELETE);
      }
    }
  }

  private void extractParamAndBody(Method method, Object[] args, MethodInfo methodInfo) {
    Map<String, Object> params = new LinkedHashMap<>();
    methodInfo.setParams(params);

    Parameter[] parameters = method.getParameters();
    for (int i = 0; i < parameters.length; i++) {
      PathVariable pathVariableAnnotation = parameters[i].getAnnotation(PathVariable.class);
      if (pathVariableAnnotation != null) {
        params.put(pathVariableAnnotation.value(), args[i]);
      }

      RequestBody requestBodyAnnotation = parameters[i].getAnnotation(RequestBody.class);
      if (requestBodyAnnotation != null) {
        methodInfo.setBody((Mono<?>) args[i]);
        methodInfo.setBodyElementType(extractElementType(parameters[i].getParameterizedType()));
      }
    }
  }

  private void extractReturnInfo(Method method, MethodInfo methodInfo) {
    methodInfo.setReturnFlux(method.getReturnType().isAssignableFrom(Flux.class));
    methodInfo.setReturnElementType(extractElementType(method.getGenericReturnType()));
  }

  private Class<?> extractElementType(Type type) {
    Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
    return (Class<?>) actualTypeArguments[0];
  }
}
