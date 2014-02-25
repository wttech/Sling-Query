package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.TestUtils.assertResourceListEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;
import com.cognifide.sling.query.api.SearchStrategy;

public class FindTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testFind() {
		SlingQuery query = $(tree.getChild("application/configuration/labels")).find();
		assertResourceSetEquals(query.iterator(), "jcr:content", "configParsys", "tab", "tab_0", "items",
				"items", "localizedtext", "text", "text_0", "text", "lang");
	}

	@Test
	public void testFindWithFilter() {
		SlingQuery query = $(tree.getChild("application/configuration/labels")).find(
				"cq-commons/config/components/text");
		assertResourceSetEquals(query.iterator(), "text", "text");
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

	@Test
	public void testBfsFind() {
		SlingQuery query = $(tree.getChild("application/configuration/labels")).searchStrategy(
				SearchStrategy.BFS).find("");
		assertResourceListEquals(query.iterator(), "jcr:content", "configParsys", "tab", "tab_0", "items",
				"items", "localizedtext", "text", "text_0", "text", "lang");
	}

	@Test
	public void testDfsFind() {
		SlingQuery query = $(tree.getChild("application/configuration/labels")).searchStrategy(
				SearchStrategy.DFS).find("");
		assertResourceListEquals(query.iterator(), "jcr:content", "configParsys", "tab", "items",
				"localizedtext", "lang", "text", "tab_0", "items", "text_0", "text");
	}
}
