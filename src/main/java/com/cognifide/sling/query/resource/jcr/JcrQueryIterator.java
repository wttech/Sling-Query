package com.cognifide.sling.query.resource.jcr;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.sling.query.iterator.AbstractIterator;

public class JcrQueryIterator extends AbstractIterator<Resource> {

	private final ResourceResolver resolver;

	private final Iterator<String> queries;

	private Iterator<Resource> currentIterator;

	public JcrQueryIterator(String selector, Resource root) {
		queries = JcrSelectorParser.parse(selector, root.getPath()).iterator();
		resolver = root.getResourceResolver();
	}

	@Override
	protected Resource getElement() {
		if (currentIterator == null) {
			if (queries.hasNext()) {
				currentIterator = resolver.findResources(queries.next(), "JCR-SQL2");
			} else {
				return null;
			}
		}
		if (currentIterator.hasNext()) {
			return currentIterator.next();
		} else {
			currentIterator = null;
			return getElement();
		}
	}
}
