package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class SiblingsTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testSiblings() {
		SlingQuery query = $(tree.getChild("application")).siblings();
		assertResourceSetEquals(query.iterator(), "jcr:content", "application", "home");
	}

	@Test
	public void testFilteredChildren() {
		SlingQuery query = $(tree.getChild("application")).siblings("cq:Page");
		assertResourceSetEquals(query.iterator(), "application", "home");
	}

	@Test
	public void testNoSiblings() {
		SlingQuery query = $(tree.getChild("application")).siblings("cq:Undefined");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testRootSiblings() {
		SlingQuery query = $(tree).siblings();
		assertResourceSetEquals(query.iterator(), "/");
	}
}
