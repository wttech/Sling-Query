package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class NextPrevAllTest {

	private static final String PAR_PATH = "home/java/labels/jcr:content/par";

	private Resource tree = TestUtils.getTree();

	@Test
	public void testNextAll() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue_0")).nextAll();
		assertResourceSetEquals(query.iterator(), "configvalue_1", "configvalue_2");
	}

	@Test
	public void testPrevAll() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue_0")).prevAll();
		assertResourceSetEquals(query.iterator(), "richtext", "configvalue");
	}

	@Test
	public void testNextAllFiltered() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue")).nextAll(
				"demo/core/components/configValue");
		assertResourceSetEquals(query.iterator(), "configvalue_0", "configvalue_1", "configvalue_2");
	}

	@Test
	public void testPrevAllFiltered() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue_0")).prevAll(
				"demo/core/components/richtext");
		assertResourceSetEquals(query.iterator(), "richtext");
	}

	@Test
	public void testNextAllInvalidFiltered() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue")).nextAll("cq:Undefined");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testPrevAllInvalidFiltered() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue")).prevAll("cq:Undefined");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testNextAllOnLast() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("configvalue_2")).nextAll();
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testPrevAllOnFirst() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH).getChild("richtext")).prevAll();
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testNextAllOnRoot() {
		SlingQuery<Resource> query = $(tree).nextAll();
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testPrevAllOnRoot() {
		SlingQuery<Resource> query = $(tree).prevAll();
		assertEmptyIterator(query.iterator());
	}
}