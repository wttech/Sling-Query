package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class AddTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testSimpleAdd() {
		SlingQuery query = $(tree.getChild("application")).add(tree.getChild("home"));
		assertResourceSetEquals(query.iterator(), "application", "home");
	}

	@Test
	public void testAddToChildren() {
		SlingQuery query = $(tree).children("cq:Page").add(tree.getChild("home/java"));
		assertResourceSetEquals(query.iterator(), "application", "home", "java");
	}

	@Test
	public void testAddedChildren() {
		SlingQuery query = $(tree).add(tree.getChild("home/java")).children("cq:Page");
		assertResourceSetEquals(query.iterator(), "application", "home", "email", "labels", "navigation");
	}

	@Test
	public void testAddIterable() {
		SlingQuery query1 = $(tree).children("cq:Page");
		SlingQuery query2 = $(tree.getChild("home/java")).children("cq:Page");
		assertResourceSetEquals(query1.add(query2).iterator(), "application", "home", "email", "labels",
				"navigation");
	}

}
