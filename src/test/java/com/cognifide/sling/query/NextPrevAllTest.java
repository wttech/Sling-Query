package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceListEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class NextPrevAllTest {

	private static final String PAR_PATH = "home/java/labels/jcr:content/par";

	private Resource tree = TestUtils.getTree();

	@Test
	public void testNextAll() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue_0")).nextAll();
		assertResourceListEquals(query.iterator(), "configvalue_1", "configvalue_2");
	}

	@Test
	public void testPrevAll() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue_0")).prevAll();
		assertResourceListEquals(query.iterator(), "richtext", "configvalue");
	}

	@Test
	public void testNextAllFiltered() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue")).nextAll(
				"demo/core/components/configValue");
		assertResourceListEquals(query.iterator(), "configvalue_0", "configvalue_1", "configvalue_2");
	}

	@Test
	public void testPrevAllFiltered() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue_0")).prevAll(
				"demo/core/components/richtext");
		assertResourceListEquals(query.iterator(), "richtext");
	}

	@Test
	public void testNextAllInvalidFiltered() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue")).nextAll("cq:Undefined");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testPrevAllInvalidFiltered() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue")).prevAll("cq:Undefined");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testNextAllOnLast() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).nextAll();
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testPrevAllOnFirst() {
		SlingQuery query = $(tree.getChild(PAR_PATH).getChild("richtext")).prevAll();
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testNextAllOnRoot() {
		SlingQuery query = $(tree).nextAll();
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testPrevAllOnRoot() {
		SlingQuery query = $(tree).prevAll();
		assertEmptyIterator(query.iterator());
	}
}