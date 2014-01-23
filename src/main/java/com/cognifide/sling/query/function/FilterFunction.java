package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;

public class FilterFunction implements IteratorToIteratorFunction {

	private final ResourcePredicate predicate;

	public FilterFunction(ResourcePredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public Iterator<Resource> apply(Iterator<Resource> input) {
		return new FilteringIteratorWrapper(input, predicate);
	}

}
