package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class NotTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testNot() {
		SlingQuery<Resource> query = $(tree).children().not("cq:Page");
		assertResourceSetEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotWithModifier() {
		SlingQuery<Resource> query = $(tree).children("cq:Page").not(":first");
		assertResourceSetEquals(query.iterator(), "home");
	}

	@Test
	public void testNotOnEmptyCollection() {
		SlingQuery<Resource> query = $().not("cq:Page");
		assertEmptyIterator(query.iterator());
	}
}
