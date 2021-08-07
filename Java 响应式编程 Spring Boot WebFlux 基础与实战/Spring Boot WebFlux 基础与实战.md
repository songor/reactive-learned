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

### Stream 流编程

**创建 Stream**

```java
list.stream();
list.parallelStream();

Arrays.stream(new int[] {1, 2, 3});

IntStream.of(1, 2, 3);
IntStream.range(0, 5);

new Random().ints().limit(5);

Random r = new Random();
Stream.generate(() -> r.nextInt()).limit(5);
```

**中间操作**

无状态：map / mapToXxx / flatMap / flatMapToXxx / filter / peek / unordered

有状态：distinct / sorted / limit / skip

```java
String str = "hello world";
// IntStream 不是 Stream 子类
Stream.of(str.split(" ")).flatMap(s -> s.chars().boxed())
    .forEach(i -> System.out.print((char) i.intValue()));
```

**终止操作**

非短路：forEach / forEachOrdered / collect / toArray / reduce / min / max / count

短路：findFirst / findAny / allMatch / anyMatch / noneMatch

```java
Stream.of(str.split(" ")).map(String::length).reduce(0, Integer::sum);
```

**并行流**

```java
ForkJoinPool pool = new ForkJoinPool(8);
pool.submit(() -> IntStream.range(0, 100).parallel()
    .peek(i -> System.out.println(Thread.currentThread().getName() + " " + i)).count());
pool.shutdown();
```

**收集器**

```java
orders.stream().map(Order::getId).collect(Collectors.toCollection(HashSet::new));

orders.stream().collect(Collectors.summarizingInt(Order::getAmount));

orders.stream().collect(Collectors.partitioningBy(order -> order.getAmount() > 5000));

orders.stream().collect(Collectors.groupingBy(Order::getType));

orders.stream().collect(Collectors.groupingBy(Order::getType, Collectors.counting()));
```

### Spring WebFlux

[在 Windows 上安装 MongoDB 社区版](https://www.docs4dev.com/docs/zh/mongodb/v3.6/reference/tutorial-install-mongodb-on-windows.html)

