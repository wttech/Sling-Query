## Find all pages with given template

```java
$(resourceResolver)
  .find("cq:PageContent[cq:template=/apps/geometrixx/templates/homepage]")
  .parent()
```

* `$(resourceResolver)` creates a collection containing `/`
* `find()` iterates over the whole subtree
