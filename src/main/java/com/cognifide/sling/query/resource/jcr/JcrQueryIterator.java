package com.cognifide.sling.query.resource.jcr;

import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.sling.query.iterator.AbstractIterator;
import com.cognifide.sling.query.resource.jcr.query.JcrQueryBuilder;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class JcrQueryIterator extends AbstractIterator<Resource> {

	private final ResourceResolver resolver;

	private final String query;

	private Iterator<Resource> currentIterator;

	public JcrQueryIterator(List<SelectorSegment> segments, Resource root, JcrTypeResolver typeResolver) {
		JcrQueryBuilder builder = new JcrQueryBuilder(typeResolver);
		query = builder.buildQuery(segments, root.getPath());
		resolver = root.getResourceResolver();
	}

	@Override
	protected Resource getElement() {
		if (currentIterator == null) {
			currentIterator = resolver.findResources(query, "JCR-SQL2");
		}
		if (currentIterator.hasNext()) {
			return currentIterator.next();
		} else {
			return null;
		}
	}
}
