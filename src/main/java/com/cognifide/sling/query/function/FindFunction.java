package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.TreeIterator;
import com.cognifide.sling.query.predicate.FilterPredicate;

public class FindFunction implements ResourceToIteratorFunction {

	private final FilterPredicate predicate;

	public FindFunction(FilterPredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public Iterator<Resource> apply(Resource resource) {
		return new TreeIterator(resource, predicate);
	}
}