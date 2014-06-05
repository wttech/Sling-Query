## Advanced selectors

```java
r = getResource("/content/geometrixx")

$(r)
  .find("[text*=square]:not(cq:PageContent):first")
  .closest("cq:Page")
  .find("#title, #image, #par:parent")
```

* `:not()` accepts any valid selector
  * `:not(:not(:not(:first)))`
* `:parent` - only resources having children
* there is a number of operators for square brackets
* alternatives can be separated with a comma
