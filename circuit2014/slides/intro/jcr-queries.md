##  JCR

```SQL
SELECT * FROM [cq:PageContent] WHERE [sling:resourceType] = 'geometrixx/components/homepage'
```

* great for:
  * looking for attribute values
  * matching node types
  * fulltext search
* problems:
  * it's hard to model tree relations
  * we lose complex hierarchy
  * repository may become a flat database
