package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceListEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class AttributeOperatorTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testEquals() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title=CQ Commons demo]");
		assertResourceListEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotEquals() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title=123]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testContains() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title*=mmons de]");
		assertResourceListEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotContains() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title*=123]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testContainsWord() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title~=Commons]");
		assertResourceListEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotContainsWord() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title~=mmons de]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testEndsWith() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title$=demo]");
		assertResourceListEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotEndsWith() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title$=CQ]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testNotEquals2() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title!=123]");
		assertResourceListEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotNotEquals() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title!=CQ Commons demo]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testStartsWith() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title^=CQ]");
		assertResourceListEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotStartsWith() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title^=Commons]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testHas() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title]");
		assertResourceListEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotHas() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title123]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testMultipleAttributes() {
		SlingQuery query = $(tree).children("cq:PageContent[jcr:title=CQ Commons demo][jcr:createdBy=admin]");
		assertResourceListEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotMultipleAttributes() {
		SlingQuery query = $(tree).children(
				"cq:PageContent[jcr:title=CQ Commons demo aaa][jcr:createdBy=admin]");
		assertEmptyIterator(query.iterator());
	}
}
