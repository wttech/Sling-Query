package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import java.util.Collections;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class NotTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testNot() {
		SlingQuery query = $(tree).children().not("cq:Page");
		assertResourceSetEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotResources() {
		SlingQuery query = $(tree).children().not(
				$(tree.getChild("jcr:content"), tree.getChild("application")));
		assertResourceSetEquals(query.iterator(), "home");
	}

	@Test
	public void testNotEmptyResources() {
		SlingQuery query = $(tree).children().not(Collections.<Resource>emptyList());
		assertResourceSetEquals(query.iterator(), "jcr:content", "application", "home");
	}

	@Test
	public void testNotWithModifier() {
		SlingQuery query = $(tree).children("cq:Page").not(":first");
		assertResourceSetEquals(query.iterator(), "home");
	}

	@Test
	public void testNotOnEmptyCollection() {
		SlingQuery query = $().not("cq:Page");
		assertEmptyIterator(query.iterator());
	}
}
