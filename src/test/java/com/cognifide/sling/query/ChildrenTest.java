package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class ChildrenTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testChildren() {
		SlingQuery query = $(tree).children();
		assertResourceSetEquals(query.iterator(), "jcr:content", "application", "home");
	}

	@Test
	public void testNameChildren() {
		SlingQuery query = $(tree).children("cq:Page#application");
		assertResourceSetEquals(query.iterator(), "application");
	}

	@Test
	public void testFilteredChildren() {
		SlingQuery query = $(tree).children("cq:Page");
		assertResourceSetEquals(query.iterator(), "application", "home");
	}

	@Test
	public void testNoChildren() {
		SlingQuery query = $(tree.getChild("jcr:content")).children();
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testLeafChildren() {
		SlingQuery query = $(tree.getChild("jcr:content")).children();
		assertEmptyIterator(query.iterator());
	}
}
