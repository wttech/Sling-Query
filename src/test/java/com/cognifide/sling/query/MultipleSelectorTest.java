package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class MultipleSelectorTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testTwoNames() {
		SlingQuery query = $(tree).children("#application, #home");
		assertResourceSetEquals(query.iterator(), "application", "home");
	}

	@Test
	public void testEverything() {
		SlingQuery query = $(tree).children(":not(#application), #application");
		assertResourceSetEquals(query.iterator(), "jcr:content", "application", "home");

		query = $(tree).children("#application, :not(#application)");
		assertResourceSetEquals(query.iterator(), "jcr:content", "application", "home");
	}
}
