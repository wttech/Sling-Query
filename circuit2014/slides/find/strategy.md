## Search strategy

```java
r = getResource("/content/geometrixx/en")

result = $(r)
  .searchStrategy(DFS)
  .find("cq:Page")

for (Resource c : result) {
  println c.path
}
```

* strategies: `DFS`, `BFS`, `QUERY`
* `QUERY` tries to rewrite `find()` selector into JCR-SQL2
  * the result is filtered once more
