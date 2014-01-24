package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertResourceListEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class HierarchySelectorTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testChildrenWithAttribute() {
		SlingQuery query = $(tree.getChild("home")).children("cq:Page > cq:PageContent[jcr:title=Java]");
		assertResourceListEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testDescendant() {
		SlingQuery query = $(tree.getChild("home")).children(
				"cq:Page [jcr:title=E-mail] demo/core/components/richtext");
		assertResourceListEquals(query.iterator(), "richtext");
	}

	@Test
	public void testNextAdjacent() {
		SlingQuery query = $(tree).children("cq:PageContent + cq:Page");
		assertResourceListEquals(query.iterator(), "application");
	}

	@Test
	public void testNextSiblings() {
		SlingQuery query = $(tree).children("cq:PageContent ~ cq:Page");
		assertResourceListEquals(query.iterator(), "application", "home");
	}
}
