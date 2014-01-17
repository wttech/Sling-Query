package com.cognifide.sling.query.operation;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.Operation;
import com.cognifide.sling.query.iterator.TreeIterator;
import com.cognifide.sling.query.predicate.FilterPredicate;

public class FindOperation implements Operation {

	private final FilterPredicate predicate;

	public FindOperation(FilterPredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public Iterator<Resource> getResources(Resource resource) {
		return new TreeIterator(resource, predicate);
	}
}