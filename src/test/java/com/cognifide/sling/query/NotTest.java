package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceListEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class NotTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testNot() {
		SlingQuery query = $(tree).children().not("cq:Page");
		assertResourceListEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotWithModifier() {
		SlingQuery query = $(tree).children("cq:Page").not(":first");
		assertResourceListEquals(query.iterator(), "home");
	}

	@Test
	public void testNotOnEmptyCollection() {
		SlingQuery query = $().not("cq:Page");
		assertEmptyIterator(query.iterator());
	}
}
