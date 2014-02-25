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
		SlingQuery<Resource> query = $(tree.getChild("home")).children("cq:Page > cq:PageContent[jcr:title=Java]");
		assertResourceSetEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testDescendant() {
		SlingQuery<Resource> query = $(tree.getChild("home")).children(
				"cq:Page [jcr:title=E-mail] demo/core/components/richtext");
		assertResourceSetEquals(query.iterator(), "richtext");
	}

	@Test
	public void testNextAdjacent() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent + cq:Page");
		assertResourceSetEquals(query.iterator(), "application");
	}

	@Test
	public void testNextSiblings() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent ~ cq:Page");
		assertResourceSetEquals(query.iterator(), "application", "home");
	}
}
