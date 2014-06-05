## Get all text components from the parsys

```java
r = getResource("/content/geometrixx/en/jcr:content/rightpar/teaser")

SlingQuery collection = $(r)
  .closest("cq:PageContent")
  .find("foundation/components/parsys#par")
  .children("foundation/components/text")

for (Resource c : collection) {
  println c.path
}
```

* each method returns new collection
* `SlingQuery` object implements `Iterable`
