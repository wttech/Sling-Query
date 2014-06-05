## Siblings but not me

```java
r = getResource("/content/geometrixx/en/products/mandelbrot/jcr:content/par/image")

myPage = $(r).closest("cq:Page")

result = myPage
  .siblings("cq:Page")
  .not(myPage)
```

* the SlingQuery collection is immutable
  * each method returns a new collection
* any `Iterable<Resource>` may be used as a filter
