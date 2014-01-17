package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;

public class ChildrenFunction implements ResourceToIteratorFunction {

	private final ResourcePredicate predicate;

	public ChildrenFunction(ResourcePredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public Iterator<Resource> apply(Resource resource) {
		Iterator<Resource> children = resource.listChildren();
		return new FilteringIteratorWrapper(children, predicate);
	}
}