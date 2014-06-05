## Random image

```java
rnd = new java.util.Random()
r = getResource("/content/dam/geometrixx/travel")

$(r)
  .children("dam:Asset")
  .filter({ rnd.nextFloat() > 0.9 } as Predicate)
  .first()
```

* in Java it'd look like this:

```java
// ...
  .filter(new Predicate<Resource>() {
    @Override
    public boolean accepts(Resource resource) {
      return rnd.nextFloat() > 0.9;
    }
  });
```
