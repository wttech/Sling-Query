# SlingQuery

SlingQuery is a Sling resource tree traversal framework inspired by the [jQuery](http://api.jquery.com/category/traversing/). It doesn't use any JCR queries underneath.

Features:

* lightweight,
* lazy evaluation,
* fluent API,
* no external dependencies.

Sample code:

	// find images on the direct children of the root page in the current
	// geometrixx language branch

	SlingQuery query = $(currentResource)
		.closest("cq:Page[jcr:content/cq:template=/apps/geometrixx/templates/homepage]")
		.children("cq:Page")
		.children("cq:PageContent")
		.find("foundation/components/image");

	for (Resource resource : collection) {
    	...
	}
