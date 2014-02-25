package com.cognifide.sling.query.resource.jcr;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.sling.query.iterator.AbstractIterator;

public class JcrTreeIterator extends AbstractIterator<Resource> {

	private final ResourceResolver resolver;

	private final String query;

	private Iterator<Resource> currentIterator;

	public JcrTreeIterator(String selector, Resource root) {
		query = JcrSelectorParser.parse(selector, root.getPath());
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
