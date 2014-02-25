package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class HasTest {

	private static final String PAR_PATH = "home/java";

	private Resource tree = TestUtils.getTree();

	@Test
	public void testHas() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH)).children().has("demo/core/components/configValue");
		assertResourceSetEquals(query.iterator(), "labels");
	}
}
