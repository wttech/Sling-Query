## Lazy list

```java
r = getResource("/content")

List<Resource> list = $(r)
  .find()
  .asList()

println list.get(0)
println list.get(5)
```

* `asList()` returns the `List<>` object
* it's a lazy list, wrapping the iterator
* the list advances the iterator only when it has to
* already iterated elements are cached in the internal array
* there are some eager methods though, like `size()` or `lastIndexOf()`
