// http://api.jquery.com/category/traversing/
// http://api.jquery.com/category/selectors/

SlingCollection collection = $(resource)
    .closest("cq:Page[jcr:content/cq:template=myapp/templates/mytemplate]")
    .children("cq:Page")
    .find("myapp/component/mycomponent")
    .filter(somePredicate)
    .slice(5);

for (Resource resource : collection) {
    ...
}
