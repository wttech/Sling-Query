package com.cognifide.sling.query;

import static com.cognifide.sling.query.api.SlingQuery.$;

import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Test;

public class MapTest {

	private static final String PAR_PATH = "home/java/labels/jcr:content/par";

	private Resource tree = TestUtils.getTree();

	@Test
	public void testMap() {
		@SuppressWarnings("rawtypes")
		Iterable<Map> iterable = $(tree.getChild(PAR_PATH)).children().map(Map.class);
		for (Map<?, ?> m : iterable) {
			Assert.assertEquals("nt:unstructured", m.get("jcr:primaryType"));
		}
	}
}
