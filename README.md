# SlingQuery
[![Build Status](https://travis-ci.org/Cognifide/Sling-Query.png?branch=master)](https://travis-ci.org/Cognifide/Sling-Query)

SlingQuery is a Sling resource tree traversal tool inspired by the [jQuery](http://api.jquery.com/category/traversing/tree-traversal/).

## Introduction

Recommended way to find resources in the Sling repository is using tree-traversal methods, like `listChildren()` and `getParent()` rather than JCR queries. The latter are great for listing resources with given properties, but we can't leverage the repository tree structure with such queries. On the other hand, using tree-traversal method is quite verbose. Consider following code that takes an resource and returns it first ancestor being `cq:Page` with given `jcr:content/cq:template` attribute:

    Resource resource = ...;
    while ((resource = resource.getParent()) != null) {
        if (!resource.isResourceType("cq:Page")) {
            continue;
        }
        Resource template = resource.getChild("jcr:content/cq:template");
        if (template != null && "my/template".equals(template.adaptTo(String.class))) {
            break;
        }
    }
    if (resource != null) {
        // we've found appropriate ancestor
    }

SlingQuery is a tool that helps creating such queries in a more concise way. Above code could be written as:

    $(resource).closest("cq:Page[jcr:content/cq:template=my/template]")

Dollar sign is a static method that takes the resource array and creates SlingQuery object. The `closest()` method returns first ancestor matching the selector string passed as the argument.

SlingQuery is inspired by the jQuery framework. jQuery is the source of method names, selector string syntax and the dollar sign method used as a collection constructor.

### Collections

`SlingQuery` class represents a collection of resources. Basic collection can be created explicitly via a dollar method:

    $(resource1, resource2, resource3)
    
Above method creates a new `SlingQuery` object that consists of 3 resources. This object implements `Iterable<Resource>` interface, so can be used in foreach statements directly:

    for (Resource resource in $(...)) { }
    
### Operations

`SlingQuery` class defines a number of methods which can be used to transform current collection into a new one. Following code:

    $(resource1, resource2).parent()

will replace each resource with its direct parent. If some resource is a repository root, it will be skipped. Some methods replace each resource with another resource (eg. `parent()` or `closest()`). Other methods can replace each resource with a set of resources:

    $(resource1, resource2).children();
    
Resulting object will contain direct children of both `resource1` and `resource2` objects. There are also methods that doesn't add any new resources, but removes existing:

    $(resource1, resource2).first();
    
Methods can be chained to create more complex query. Eg. following code will return last direct child of the `resource`:

    $(resource).children().last();
    
#### Laziness

All operations are lazy (except `prev()` and sometimes `not()`). It means that `SlingQuery` won't read any resources until it's actually necessary. Example:

    $(resource).children().children().first();

`children().children()` construction reads all grand-children of the given resource. However, the last method limits the output to the first found resource. As a result, `SlingQuery` won't iterate over all children and grand-children, but it will simply take the first child of the `resource` and return its first child.

#### Immutability

`SlingQuery` object is immutable and each operation creates a new one. We can "freeze" some collection before performing more operations on it:

    SlingQuery children = $(resource).children();
    SlingQuery firstChild = children.first();
    for (Resource child : children) { /* will display all children */ }
    for (Resource child : firstChild) { /* will display the first child */ }

### Selectors

Some operations may take an additional string selector parameter that defines a filtering. Selector could be used to define resource type, resource attributes and additional modifiers. Example selector could look like this:

    "cq:Page"
    
It will match all resources with the given resource type. Example:

    $(resource).children("cq:Page")
    
will return only children with `cq:Page` resource type. You could also filter these resources defining any number of attributes in the square brackets:

    $(resource).children("cq:Page[jcr:title=Some title][jcr:description=Some desc]")

And finally, you could add some modifiers at the end:

    $(resource).children("cq:Page[jcr:content/cq:template=my/template]:even")

Above resources will find `cq:Page` children of the resource, using template `my/template` and return not all of them, but only those with even indices (eg. if matching children of the `resource` are `page_0`, `page_1` and `page_2`, only the first and the last will be returned).

All parts of the selector are optional. In fact, an empty string (`""`) is a valid selector, accepting all resources. However, the defined order (resource type, attributes in square brackets and modifiers) has to be followed. Example selectors:

    "foundation/components/richtext" // resource type
    "foundation/components/richtext:first" // resource type with modifier
    "[property=value][property2=value2]" // two attributes
    ":even" // modifier
    ":even:not(:first)" // two modifiers, the second one is nested

## Method list

### `$(Resource... resources)`

Create a new SlingQuery object, using passed resources as an initial collection. Example:

    $(resource); // a simple SlingQuery collection containing one resource

### `.children([selector])`

Get list of the children for each resource in the collection. Pass `selector` to filter children. Example:

    $(resource).children("cq:Page"); // get all page children of the resource
    $(resource).children().children(); // get all grand-children of the resource

### `.closest(selector)`

For each resource in the collection, return the first element matching the selector testing the resource itself and traversing up its ancestors. Example:

    $(resource).closest("cq:Page"); // find containing page, like PageManager#getContainingPage
    // let's assume that someCqPageResource is a cq:Page
    $(someCqPageResource).closest("cq:Page"); // return the same resource

### `.eq(index)`

Reduce resource collection to the one resource at the given 0-based index. Example:

    $(resource0, resource1, resource2).eq(1); // return resource1
    $(resource).children().eq(0); // return first child of the resource

### `.filter(predicate)`

Filter resource collection using given predicate object.

	final Calendar someTimeAgo = Calendar.getInstance();
	someTimeAgo.add(Calendar.HOUR, -5);

	// get children pages modified in the last 5 hours
	SlingQuery query = $(resource).children("cq:Page").filter(new ResourcePredicate() {
		@Override
		public boolean accepts(Resource resource) {
			return resource.adaptTo(Page.class).getLastModified().after(someTimeAgo);
		}
	});

### `.find([selector])`

For each resource in collection use [breadth-first search](http://en.wikipedia.org/wiki/Breadth-first_search) to return all its descendants. Please notice that invoking this method on a resource being a root of a large subtree may and will cause performance problems.

	$(resource).find("cq:Page"); // find all descendant pages

### `.first()`

Filter resource collection to the first element. Equivalent to `.eq(0)` or `.slice(0, 0)`.

    $(resource).siblings().first(); // get the first sibling of the current resource

### `.function(function, selector)`

This method allows to process resource collection using custom function and then filter the results by a selector string. First parameter has to implement one of the interfaces:

* `ResourceToResourceFunction`,
* `ResourceToIteratorFunction`,
* `IteratorToIteratorFunction`.

Example:

	// eager (not-lazy) way to reverse the collection order
	$(...).function(new IteratorToIteratorFunction() {
		public Iterator<Resource> apply(Iterator<Resource> input) {
			List<Resource> resources = new ArrayList<Resource>();
			while (input.hasNext()) {
				resources.add(input.next());
			}
			Collections.reverse(resources);
			return resources.iterator();
		}
	}, ""); // empty selector to accept all resources

### `.has(selector)`

Pick such resources from the collection that have descendant matching the selector. Example:

    $(...).children('cq:Page').has(foundation/components/richtext) // find children pages containing some richtext component

### `.last()`

Filter resource collection to the last element.

    $(resource).siblings().last(); // get the last sibling of the current resource

### `.map(Class<T> clazz)`

Transform the whole collection to a new `Iterable<T>` object, invoking `adaptTo(clazz)` method on each resource. If some resource can't be adapted to the class (eg. `adaptTo()` returns `null`), it will be skipped. Example:

    for (Page page : $(resource).parents("cq:Page").map(Page.class)) {
        // display breadcrumbs
    }

### `.next([selector])`

Return the next sibling for each resource in the collection and optionally filter it by a selector. If the selector is given, but the sibling doesn't match it, empty collection will be returned.

    // let's assume that resource have 3 children: child1, child2 and child3
    $(resource).children().first().next(); // return child2

### `.nextAll([selector])`

Return all following siblings for each resource in the collection, optionally filtering them by a selector.

    // let's assume that resource have 3 children: child1, child2 and child3
    $(resource).children().first().nextAll(); // return child2 and child3

### `.nextUntil(selector[, filter])`

Return all following siblings for each resource in the collection up to, but not including, resource matched by a selector.

    // let's assume that resource have 4 children: child1, child2, child3 and child4
    // additionaly, child4 has property jcr:title=Page
    $(resource).children().first().nextUntil("[jcr:title=Page]"); // return child2 and child3

### `.not(selector)`

Remove elements from the collection. This function will evaluate the collection eagerly (=will create an ArrayList with the whole collection) if the selector contains some modifiers (functions starting with colon, like `:first` or `:eq(2)`).

    $(resource).children().not("cq:Page"); // remove all cq:Pages from the collection
    $(resource).children().not(":first").not(":last"); // remove the first and the last element of the collection

### `.parent()`

Replace each element in the collection with its parent.

    $(resource).find("cq:PageContent[jcr:title=My page]:first").parent(); // find the parent of the first `cq:PageContent` resource with given attribute in the subtree
    
### `.parents([selector])`

For each element in the collection find its all ancestor, optionally filtered them by a selector.

    ($resource).parents("cq:Page"); // create page breadcrumbs for the given resources

### `.parentsUntil(selector[, filter])`

For each element in the collection find all of its ancestors until the predicate is met, optionally filter by a selector.

    ($currentResource).parentsUntil("cq:Page"); // find all ancestor components on the current page
    
### `.prev([selector])`

Return the previous sibling for each resource in the collection and optionally filter it by a selector. If the selector is given, but the sibling doesn't match it, empty collection will be returned.

    // let's assume that resource have 3 children: child1, child2 and child3
    $(resource).children().last().prev(); // return child2

### `.prevAll([selector])`

Return all preceding siblings for each resource in the collection, optionally filtering them by a selector.

    // let's assume that resource have 3 children: child1, child2 and child3
    $(resource).children().last().prevAll(); // return child1 and child2

### `.prevUntil(selector[, filter])`

Return all preceding siblings for each resource in the collection up to, but not including, resource matched by a selector.

    // let's assume that resource have 4 children: child1, child2, child3 and child4
    // additionally, child1 has property jcr:title=Page
    $(resource).children().last().prevUntil("[jcr:title=Page]"); // return child2 and child3

### `.siblings([selector])`

Return siblings for the given resources, optionally filtered by a selector.

    $(resource).closest("cq:Page").siblings("cq:Page"); // return all sibling pages

### `.slice(from[, to])`

Reduce the collection to a sub-collection specified by a given range. Both `from` and `to` are inclusive and 0-based indices. If the `to` parameter is not specified, the whole sub-collection starting with `from` will be returned.

    // let's assume that resource have 4 children: child1, child2, child3 and child4
    $(resource).children().slice(1, 2); // return child1 and child2

## Selector

Selector consists of three parts:

### Resource type

Resource type, which could be a `sling:resourceType`, like `foundation/components/richtext` or the underlying JCR node type, like `cq:Page` or `nt:unstructured`. In the latter case, SlingQuery takes types hierarchy into consideration (eg. `nt:base` matches everything). JCR mixin types could be used as well.

### Attributes

After the resource type (or instead of it) one could pass a number of filtering attributes. Each attribute has following form: `[property=value]`. Passing multiple attributes will match only those resources that have all of them set. Property name could contain `/`. In this case property will be taken from the child resource, eg.:

    $(resource).children("cq:Page[jcr:content/jcr:title=My title]")
    
will return only children of type `cq:Page` that have sub-resource called `jcr:content` with property `jcr:title` set to `My title`.

### Modifiers

At the end of the selector one could define any number of modifiers that will be used to filter out the resources matched by the resource type and attributes. Each modifier starts with colon, some of them accepts a parameter set in parentheses. Example:

    $(resource).children("cq:Page:first");
    $(resource).children("cq:Page:eq(0)"); // the same
    $(resource).children(":first"); // modifier can be used alone

It is important that modifier filters out sub-collection created for each node, before it is merged. Eg.:, there is a difference between:

    $(resource1, resource2).children().first();

and

    $(resource1, resource2).children(":first");
    
In the first case we create a new collection consisting of children of the `resource1` and `resource2` and then we get the first element of the merged collection. On the other hand, the second example takes *first child* of each resource and creates a collection from them.

## Modifier list

### `:eq(index)`

Reduce the set of matched elements to the one at the specified 0-based index. Example:

    $(...).find("foundation/components/richtext:eq(2)"); // find the third richtext in the subtree

### `:even`

Reduce the set of matched elements to those which indexes are even numbers:

    $(...).children("cq:Page:even"); // get even children pages for each resource in the collection

### `:first`

Reduce the set of matched elements to the first one:

    $(...).find("foundation/components/richtext:first"); // find the first richtext in the subtree

### `:gt(index)`

Reduce the set of matched elements to those which indexes are greater than the argument:

    $(...).children("cq:Page:gt(2)"); // filter out first 3 pages

### `:has(selector)`

Reduce the set of the matched elements to those which have descendant matching the selector:

    $(...).children("cq:Page:has(foundation/components/richtext)]"); // get children pages containing richtext component

### `:last`

Reduce the set of matched elements to the last one:

    $(...).children("cq:Page:last"); // find the last child page for each resource

### `:lt(index)`

Reduce the set of matched elements to those which indexes are lesser than the argument:

    $(...).children("cq:Page:lt(3)"); // get first 3 matches

### `:not(selector)`

Reduce the set of matched elements to those which doesn't match the selector. The selector may contain other modifiers as well, however in this case the function will be evaluated eagerly:

    $(...).children(":not(:parent)"); // resources that doesn't contain any children

### `:odd`

Reduce the set of matched elements to those which indexes are odd numbers:

    $(...).children("cq:Page:odd"); // get odd children pages for each resource in the collection

### `:parent`

Reduce the set of the matched elements to those which have any descendant resource.

    $(...).children(":parent]"); // get children resources containing any resource

## External resources

* See the [Apache Sling website](http://sling.apache.org/) for the Sling reference documentation. Apache Sling, Apache and Sling are trademarks of the [Apache Software Foundation](http://apache.org).
