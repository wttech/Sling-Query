package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertEmptyIterator;
import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;

import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.api.SlingQuery;

public class FilterTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testFilter() {
		SlingQuery query = $(tree).find().filter(new ResourcePredicate() {
			@Override
			public boolean accepts(Resource resource) {
				return "configParsys".equals(resource.getName());
			}
		});
		assertResourceSetEquals(query.iterator(), "configParsys");
	}

	@Test
	public void testFilterOnEmptyCollection() {
		SlingQuery query = $().filter(new ResourcePredicate() {
			@Override
			public boolean accepts(Resource resource) {
				return true;
			}
		});
		assertEmptyIterator(query.iterator());
	}
}
