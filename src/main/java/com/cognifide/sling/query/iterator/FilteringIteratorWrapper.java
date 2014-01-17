package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;

public class FilteringIteratorWrapper extends AbstractResourceIterator {

	private final Iterator<Resource> iterator;

	private final ResourcePredicate predicate;

	public FilteringIteratorWrapper(Iterator<Resource> iterator, ResourcePredicate predicate) {
		this.iterator = iterator;
		this.predicate = predicate;
	}

	@Override
	protected Resource getResource() {
		while (iterator.hasNext()) {
			Resource resource = iterator.next();
			if (predicate.accepts(resource)) {
				return resource;
			}
		}
		return null;
	}
}
