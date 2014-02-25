package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class LastTest {

	private static final String PAR_PATH = "home/java/labels/jcr:content/par";

	private Resource tree = TestUtils.getTree();

	@Test
	public void testLast() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH)).children().last();
		assertResourceSetEquals(query.iterator(), "configvalue_2");
	}

	@Test
	public void testLastOnEmptyCollection() {
		SlingQuery<Resource> query = $().last();
		assertEmptyIterator(query.iterator());
	}
}
