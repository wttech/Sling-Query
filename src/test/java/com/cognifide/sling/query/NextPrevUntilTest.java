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
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue")).nextUntil("[key=unknownKey]");
		assertResourceSetEquals(query.iterator(), "configvalue_0", "configvalue_1");
	}

	@Test
	public void testPrevUntil() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).prevUntil("[key=helloWorld]");
		assertResourceSetEquals(query.iterator(), "configvalue_0", "configvalue_1");
	}

	@Test
	public void testNextUntilFiltered() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue")).nextUntil("[key=unknownKey]",
				"demo/core/components/configValue");
		assertResourceSetEquals(query.iterator(), "configvalue_0", "configvalue_1");
	}

	@Test
	public void testPrevUntilFiltered() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).prevUntil("[key=helloWorld]",
				"demo/core/components/configValue");
		assertResourceSetEquals(query.iterator(), "configvalue_0", "configvalue_1");
	}

	@Test
	public void testNextUntilInvalidFiltered() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue")).nextUntil("[key=unknownKey]",
				"cq:Undefined");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testPrevUntilInvalidFiltered() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).prevUntil("[key=helloWorld]",
				"cq:Undefined");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testNextUntilOnLast() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).nextUntil("[key=unknownKey]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testPrevUntilOnFirst() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("richtext")).prevUntil("[key=helloWorld]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testNextUntilOnRoot() {
		SlingQuery<Resource> query = $(tree).nextUntil("cq:Page");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testPrevUntilOnRoot() {
		SlingQuery<Resource> query = $(tree).prevUntil("cq:Page");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testNextUntilInvalid() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("richtext")).nextUntil("cq:Undefined");
		assertResourceSetEquals(query.iterator(), "configvalue", "configvalue_0", "configvalue_1",
				"configvalue_2");
	}

	@Test
	public void testPrevUntilInvalid() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).prevUntil("cq:Undefined");
		assertResourceSetEquals(query.iterator(), "configvalue", "configvalue_0", "configvalue_1",
				"richtext");
	}
}