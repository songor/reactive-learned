# Spring Boot WebFlux 基础与实战

### Lambda 表达式

**方法引用**

静态方法

非静态方法（对象实例 / 类名 -> 编译器将 this 设置为非静态方法的第一个参数）

构造函数

**级联表达式及柯里化**

优势：函数标准化 / 高阶函数（返回函数的函数）

```java
Function<Integer, Function<Integer, Integer> f = x -> y -> x + y;
f.apply(2).apply(3);
```

