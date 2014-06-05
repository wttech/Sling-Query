## Find method vs JCR

* `find()` is powerful but may be dangerous
* it should be used only for small subtrees
* if you want to query a large space, use JCR-SQL[2] or XPath
* if your SlingQuery processes more than 100 resources, you'll get
  a warning in the logs:

```
28.05.2014 13:35:49.942 *WARN* [0:0:0:0:0:0:0:1 [1401276949857] POST /bin/groovy
console/post.json HTTP/1.1] SlingQuery Number of processed resources exceeded 10
0. Consider using a JCR query instead of SlingQuery. More info here: http://git.
io/h2HeUQ
```
