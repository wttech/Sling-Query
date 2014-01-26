package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class ParentTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testParent() {
		SlingQuery query = $(tree.getChild("application/configuration/labels")).parent();
		assertResourceSetEquals(query.iterator(), "configuration");
	}

	@Test
	public void testRootParent() {
		SlingQuery query = $(tree).parent();
		assertEmptyIterator(query.iterator());
	}
}
