package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceListEquals;
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
		assertResourceListEquals(query.iterator(), "configvalue_0", "configvalue_1");
	}

	@Test
	public void testPrevUntil() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).prevUntil("[key=helloWorld]");
		assertResourceListEquals(query.iterator(), "configvalue_0", "configvalue_1");
	}

	@Test
	public void testNextUntilFiltered() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue")).nextUntil("[key=unknownKey]",
				"demo/core/components/configValue");
		assertResourceListEquals(query.iterator(), "configvalue_0", "configvalue_1");
	}

	@Test
	public void testPrevUntilFiltered() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).prevUntil("[key=helloWorld]",
				"demo/core/components/configValue");
		assertResourceListEquals(query.iterator(), "configvalue_0", "configvalue_1");
	}

	@Test
	public void testNextUntilInvalidFiltered() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue")).nextUntil("[key=unknownKey]",
				"cq:Undefined");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testPrevUntilInvalidFiltered() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).prevUntil("[key=helloWorld]",
				"cq:Undefined");
		assertEmptyIterator(query.iterator());
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
		assertResourceListEquals(query.iterator(), "configvalue", "configvalue_0", "configvalue_1",
				"configvalue_2");
	}

	@Test
	public void testPrevUntilInvalid() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).prevUntil("cq:Undefined");
		assertResourceListEquals(query.iterator(), "configvalue", "configvalue_0", "configvalue_1",
				"richtext");
	}
}