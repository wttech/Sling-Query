package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class NextPrevUntilTest {

	private static final String PAR_PATH = "home/java/labels/jcr:content/par";

	private Resource tree = TestUtils.getTree();

	@Test
	public void testNextUntil() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue")).nextUntil("[key=unknownKey]");
		assertResourceSetEquals(query.iterator(), "configvalue_0", "configvalue_1");
	}

	@Test
	public void testNextUntilResource() {
		Resource resource = tree.getChild(PAR_PATH).getChild("configvalue_2");
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue")).nextUntil($(resource));
		assertResourceSetEquals(query.iterator(), "configvalue_0", "configvalue_1");
	}

	@Test
	public void testPrevUntil() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).prevUntil("[key=helloWorld]");
		assertResourceSetEquals(query.iterator(), "configvalue_0", "configvalue_1");
	}

	@Test
	public void testPrevUntilResource() {
		Resource resource = tree.getChild(PAR_PATH).getChild("configvalue");
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).prevUntil($(resource));
		assertResourceSetEquals(query.iterator(), "configvalue_0", "configvalue_1");
	}

	@Test
	public void testNextUntilOnLast() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).nextUntil("[key=unknownKey]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testPrevUntilOnFirst() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("richtext")).prevUntil("[key=helloWorld]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testNextUntilOnRoot() {
		SlingQuery query = $(tree).nextUntil("cq:Page");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testPrevUntilOnRoot() {
		SlingQuery query = $(tree).prevUntil("cq:Page");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testNextUntilInvalid() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("richtext")).nextUntil("cq:Undefined");
		assertResourceSetEquals(query.iterator(), "configvalue", "configvalue_0", "configvalue_1",
				"configvalue_2");
	}

	@Test
	public void testPrevUntilInvalid() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).prevUntil("cq:Undefined");
		assertResourceSetEquals(query.iterator(), "configvalue", "configvalue_0", "configvalue_1", "richtext");
	}
}