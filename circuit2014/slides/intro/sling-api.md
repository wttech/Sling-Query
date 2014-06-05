## Sling API

```java
Resource parent = myResource.getParent();
  for (Resource child : parent.getChildren()) {
	if (child.adaptTo(ValueMap.class).containsKey("myProperty")) {
  	//...
	}
}
```

* efficient, especially for denormalized and well-structured content[1]
* easy to use
* but:
	* a lot of `while()`s, iterators and nullchecks
	* code complexity is growing fast

---
[1] [Efficient content structures and queries in CRX](http://www.pro-vision.de/content/medialib/pro-vision/production/adaptto/2012/adaptto2012-efficient-content-structures-and-queries-in-crx-marc/_jcr_content/renditions/rendition.file/adaptto2012-efficient-content-structures-and-queries-in-crx-marcel-reutegger.pdf)
