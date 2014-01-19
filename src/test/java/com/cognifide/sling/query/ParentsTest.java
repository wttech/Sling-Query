package com.cognifide.sling.query;

import static com.cognifide.sling.query.api.SlingQuery.$;
import static com.cognifide.sling.query.TestUtils.assertResourceListEquals;
import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class ParentsTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testParents() {
		SlingQuery query = $(tree.getChild("application/configuration/labels/jcr:content")).parents();
		assertResourceListEquals(query.iterator(), "labels", "configuration", "application", "/");
	}

	@Test
	public void testFilteredParents() {
		SlingQuery query = $(
				tree.getChild("application/configuration/labels/jcr:content/configParsys/tab/items"))
				.parents("cq:Page");
		assertResourceListEquals(query.iterator(), "labels", "configuration", "application", "/");
	}

	@Test
	public void testNoParents() {
		SlingQuery query = $(
				tree.getChild("application/configuration/labels/jcr:content/configParsys/tab/items"))
				.parents("cq:Undefined");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testRootParents() {
		SlingQuery query = $(tree).parents();
		assertEmptyIterator(query.iterator());
	}
}
