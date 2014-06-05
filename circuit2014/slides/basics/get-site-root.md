## SlingQuery example

```java
import static com.cognifide.sling.query.api.SlingQuery.$;
Resource resource = getResource("/content/geometrixx/en/products/triangle/jcr:content/par");

$(resource)
  .closest("cq:Page[jcr:content/cq:template=/apps/geometrixx/templates/homepage]")
```

* `$()` is a valid method name in Java,
  * it wraps resource(s) into an iterable SlingQuery collection
  * each method transforms the existing collection into a new one
* API inspired by jQuery
