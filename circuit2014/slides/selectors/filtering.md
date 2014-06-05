## Selector string

```java
r = getResource("/content/geometrixx/en/products/mandelbrot")

$(r)
  .children("cq:PageContent")
  .children("foundation/components/parsys")
  .children("#title[jcr:title=Best in class][type=large]:first")
```

* selector format
  * resource type or node type
  * `#resource-name`
  * attributes in `[]`
  * modifiers, each prepended by `:`
* all elements are optional
