package com.cognifide.sling.query;

import static com.cognifide.sling.query.api.SlingQuery.$;
import static com.cognifide.sling.query.TestUtils.assertResourceListEquals;
import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class FindTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testFind() {
		SlingQuery query = $(tree.getChild("application/configuration/labels")).find();
		assertResourceListEquals(query.iterator(), "jcr:content", "configParsys", "tab", "tab_0", "items",
				"items", "localizedtext", "text", "text_0", "text", "lang");
	}

	@Test
	public void testFindWithFilter() {
		SlingQuery query = $(tree.getChild("application/configuration/labels")).find(
				"cq-commons/config/components/text");
		assertResourceListEquals(query.iterator(), "text", "text");
	}

	@Test
	public void testLeaveFind() {
		SlingQuery query = $(
				tree.getChild("application/configuration/labels/jcr:content/configParsys/tab/items/localizedtext/lang"))
				.find();
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testEmptyFind() {
		SlingQuery query = $(tree.getChild("application/configuration/labels")).find("cq:Undefined");
		assertEmptyIterator(query.iterator());
	}
}
