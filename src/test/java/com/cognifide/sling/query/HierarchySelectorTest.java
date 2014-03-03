package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class HierarchySelectorTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testChildrenWithAttribute() {
		SlingQuery query = $(tree.getChild("home")).children("cq:Page > cq:PageContent[jcr:title=Java]");
		assertResourceSetEquals(query.iterator(), "java");
	}

	@Test
	public void testDescendant() {
		SlingQuery query = $(tree.getChild("home")).children(
				"cq:Page demo/core/components/configValue");
		assertResourceSetEquals(query.iterator(), "java");
	}

	@Test
	public void testNextAdjacent() {
		SlingQuery query = $(tree).children("cq:PageContent + cq:Page");
		assertResourceSetEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNextSiblings() {
		SlingQuery query = $(tree).children("cq:PageContent ~ cq:Page");
		assertResourceSetEquals(query.iterator(), "jcr:content");
	}
}
