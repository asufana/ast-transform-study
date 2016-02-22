

# ast-transform-study

Test01.java

```java
public class Test01 {
    private var a = "123";
}
```

Test01 transformed

```java
public class Test01 {
    public Test01() {
        super();
    }
    private java.lang.Object a = "123";
}
```

