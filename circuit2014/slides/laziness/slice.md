## Laziness

```java
result = $(resourceResolver)
  .searchStrategy(DFS)
  .find()
  .slice(10, 20)

result.toString()
```

* we don't query resources unless we need them
* invoking the SlingQuery method adds a new function to the chain
* functions are executed by the final iterator (like the one created by
  the `for()` loop)
* all functions are lazy
  * expect the `last()` function
