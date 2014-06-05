## Breadcrumbs

```java
r = getResource("/content/geometrixx/en/products/mandelbrot/overview/jcr:content/par")

Iterable<Page> breadcrumbs = $(r)
  .parents("cq:Page")
  .not("[jcr:content/hideInNav=true]")
  .map(Page.class)

for (Page p : breadcrumbs) {
  println p.title
}
```

* `map()` method creates a new `Iterable<>` adapting each resource to a given class
  * `resource.adaptTo(Page.class)`
  * approach compatible with [Sling Models](http://sling.apache.org/documentation/bundles/models.html)
