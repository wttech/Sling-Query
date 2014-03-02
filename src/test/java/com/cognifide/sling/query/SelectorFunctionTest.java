package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class SelectorFunctionTest {

	// children with indexes:
	// 0 - richtext
	// 1 - configvalue
	// 2 - configvalue_0
	// 3 - configvalue_1
	// 4 - configvalue_2
	private static final String PAR_PATH = "home/java/labels/jcr:content/par";

	private Resource tree = TestUtils.getTree();

	@Test
	public void testEq() {
		SlingQuery query = $(tree.getChild(PAR_PATH)).children(":eq(2)");
		assertResourceSetEquals(query.iterator(), "configvalue_0");
	}

	@Test
	public void testFirst() {
		SlingQuery query = $(tree.getChild(PAR_PATH)).children(":first");
		assertResourceSetEquals(query.iterator(), "richtext");
	}

	@Test
	public void testGt() {
		SlingQuery query = $(tree.getChild(PAR_PATH)).children(":gt(2)");
		assertResourceSetEquals(query.iterator(), "configvalue_1", "configvalue_2");
	}

	@Test
	public void testLt() {
		SlingQuery query = $(tree.getChild(PAR_PATH)).children(":lt(3)");
		assertResourceSetEquals(query.iterator(), "richtext", "configvalue", "configvalue_0");
	}

	@Test
	public void testHas() {
		SlingQuery query = $(tree.getChild("home/java")).children(":has([key=helloWorld])");
		assertResourceSetEquals(query.iterator(), "labels");
	}

	@Test
	public void testParent() {
		SlingQuery query = $(tree.getChild("home/java/email/jcr:content/par")).children(":parent");
		assertResourceSetEquals(query.iterator(), "email");
	}

	@Test
	public void testOdd() {
		SlingQuery query = $(tree.getChild(PAR_PATH)).children(":odd");
		assertResourceSetEquals(query.iterator(), "configvalue", "configvalue_1");
	}

	@Test
	public void testEven() {
		SlingQuery query = $(tree.getChild(PAR_PATH)).children(":even");
		assertResourceSetEquals(query.iterator(), "richtext", "configvalue_0", "configvalue_2");
	}

	@Test
	public void testSimpleNot() {
		SlingQuery query = $(tree.getChild(PAR_PATH)).children(":not(demo/core/components/richtext)");
		assertResourceSetEquals(query.iterator(), "configvalue", "configvalue_0", "configvalue_1",
				"configvalue_2");
	}

	@Test
	public void testComplexNot() {
		SlingQuery query = $(tree.getChild(PAR_PATH)).children(":not(:first)");
		assertResourceSetEquals(query.iterator(), "configvalue", "configvalue_0", "configvalue_1", "configvalue_2");
	}
}