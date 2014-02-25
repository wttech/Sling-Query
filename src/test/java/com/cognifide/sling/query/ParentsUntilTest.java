package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class ParentsUntilTest {

	private static final String PAR_PATH = "application/configuration/labels/jcr:content/configParsys/tab/items";

	private Resource tree = TestUtils.getTree();

	@Test
	public void testParentsUntilMatch() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH)).parentsUntil("cq:Page");
		assertResourceSetEquals(query.iterator(),  "jcr:content", "configParsys", "tab");
	}

	@Test
	public void testParentsUntilNoMatch() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH)).parentsUntil("cq:Undefined");
		assertResourceSetEquals(query.iterator(), "application", "configuration", "labels", "jcr:content", "configParsys", "tab", "/");
	}

	@Test
	public void testParentsUntilFromRoot() {
		SlingQuery<Resource> query = $(tree).parentsUntil("cq:Page", "cq:Undefined");
		assertResourceSetEquals(query.iterator());
	}

}
