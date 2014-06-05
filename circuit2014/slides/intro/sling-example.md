## Sling example

Find the first ancestor with a given template.

```java
String path = "/content/geometrixx/en/products/triangle/jcr:content/par";
String homeTemplate = "/apps/geometrixx/templates/homepage";

Resource resource = resourceResolver.getResource(path);
while ((resource = resource.getParent()) != null) {
    if (!resource.isResourceType("cq:Page")) {
        continue;
    }
    ValueMap map = resource.adaptTo(ValueMap.class);
    String cqTemplate = map.get("jcr:content/cq:template");
    if (homeTemplate.equals(cqTemplate)) {
        break;
    }
}
resource.getPath();
```
