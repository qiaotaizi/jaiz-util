# 添加依赖

```xml
<dependency>
    <groupId>com.jaiz.utils</groupId>
    <artifactId>jaiz-utils</artifactId>
    <version>2.0.1</version>
</dependency>
```

# 2.0.1升级说明

加入对象实例化工具,用法:

```java
public static void main(String[] args){
  new com.jaiz.utils.VOInitializer().universalInit(yourClass);
}
```

或者,需要实例化List/Set

```java
public static void main(String[] args){
 //genericClass表示集合元素泛型类
 new com.jaiz.utils.VOInitializer().universalInit(yourClass,genericClass);
}
```

或者,需要实例化Map

```java
public static void main(String[] args){
 //keyClass表示键类型,valueClass表示值类型
 new com.jaiz.utils.VOInitializer().universalInit(yourClass,keyClass,valueClass);
}
```



# 2.0.0升级说明

加入http请求工具
