package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.SlingQuery;

public class SliceTest {

	// children with indexes:
	// 0 - richtext
	// 1 - configvalue
	// 2 - configvalue_0
	// 3 - configvalue_1
	// 4 - configvalue_2
	private static final String PAR_PATH = "home/java/labels/jcr:content/par";

	private Resource tree = TestUtils.getTree();

	@Test
	public void testSlice() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH)).children().slice(2, 4);
		assertResourceSetEquals(query.iterator(), "configvalue_0", "configvalue_1", "configvalue_2");
	}

	@Test
	public void testSliceOne() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH)).children().slice(2, 2);
		assertResourceSetEquals(query.iterator(), "configvalue_0");
	}

	@Test
	public void testEq() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH)).children().eq(2);
		assertResourceSetEquals(query.iterator(), "configvalue_0");
	}

	@Test
	public void testEqOnEmpty() {
		SlingQuery<Resource> query = $().eq(0);
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testFirst() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH)).children().first();
		assertResourceSetEquals(query.iterator(), "richtext");
	}

	@Test
	public void testFirstOnEmpty() {
		SlingQuery<Resource> query = $().first();
		assertEmptyIterator(query.iterator());
	}

	@Test
	public void testSliceAll() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH)).children().slice(0, 4);
		assertResourceSetEquals(query.iterator(), "richtext", "configvalue", "configvalue_0",
				"configvalue_1", "configvalue_2");
	}

	@Test
	public void testSliceAllBigTo() {
		SlingQuery<Resource> query = $(tree.getChild(PAR_PATH)).children().slice(0, 10);
		assertResourceSetEquals(query.iterator(), "richtext", "configvalue", "configvalue_0",
				"configvalue_1", "configvalue_2");
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSliceNegativeFrom() {
		$(tree.getChild(PAR_PATH)).children().slice(-1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSliceNegativeFrom2() {
		$(tree.getChild(PAR_PATH)).children().slice(-1, 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSliceFromGreaterThanTo() {
		$(tree.getChild(PAR_PATH)).children().slice(2, 1);
	}
}