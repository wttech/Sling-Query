package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;

public class FilterFunction implements IteratorToIteratorFunction {

	private final Predicate<Resource> predicate;

	public FilterFunction(Predicate<Resource> predicate) {
		this.predicate = predicate;
	}

	@Override
	public Iterator<Resource> apply(Iterator<Resource> input) {
		return new FilteringIteratorWrapper<Resource>(input, predicate);
	}

}
