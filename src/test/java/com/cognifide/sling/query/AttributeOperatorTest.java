package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class AttributeOperatorTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testEquals() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title=CQ Commons demo]");
		assertResourceSetEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotEquals() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title=123]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testContains() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title*=mmons de]");
		assertResourceSetEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotContains() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title*=123]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testContainsWord() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title~=Commons]");
		assertResourceSetEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotContainsWord() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title~=mmons de]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testEndsWith() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title$=demo]");
		assertResourceSetEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotEndsWith() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title$=CQ]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testNotEquals2() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title!=123]");
		assertResourceSetEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotNotEquals() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title!=CQ Commons demo]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testStartsWith() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title^=CQ]");
		assertResourceSetEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotStartsWith() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title^=Commons]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testHas() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title]");
		assertResourceSetEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotHas() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title123]");
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testMultipleAttributes() {
		SlingQuery<Resource> query = $(tree).children("cq:PageContent[jcr:title=CQ Commons demo][jcr:createdBy=admin]");
		assertResourceSetEquals(query.iterator(), "jcr:content");
	}

	@Test
	public void testNotMultipleAttributes() {
		SlingQuery<Resource> query = $(tree).children(
				"cq:PageContent[jcr:title=CQ Commons demo aaa][jcr:createdBy=admin]");
		assertEmptyIterator(query.iterator());
	}
}
