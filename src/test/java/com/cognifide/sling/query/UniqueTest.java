package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertResourceListEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class UniqueTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testUnique() {
		Resource r1 = tree.getChild("home");
		Resource r2 = tree.getChild("application");
		Resource r3 = tree.getChild("home/java");

		SlingQuery query = $(r1, r1, r1, r2, r2, r3, r3, r3, r1).unique();
		assertResourceListEquals(query.iterator(), "home", "application", "java", "home");
	}
}
