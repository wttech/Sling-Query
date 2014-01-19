package com.cognifide.sling.query;

import static com.cognifide.sling.query.api.SlingQuery.$;
import static com.cognifide.sling.query.TestUtils.assertResourceListEquals;
import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class SiblingsTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testSiblings() {
		SlingQuery query = $(tree.getChild("application")).siblings();
		assertResourceListEquals(query.iterator(), "jcr:content", "application", "home");
	}

	@Test
	public void testFilteredChildren() {
		SlingQuery query = $(tree.getChild("application")).siblings("cq:Page");
		assertResourceListEquals(query.iterator(), "application", "home");
	}

	@Test
	public void testNoSiblings() {
		SlingQuery query = $(tree.getChild("application")).siblings("cq:Undefined");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testRootSiblings() {
		SlingQuery query = $(tree).siblings();
		assertResourceListEquals(query.iterator(), "/");
	}
}
