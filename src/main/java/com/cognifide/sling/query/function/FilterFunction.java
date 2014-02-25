package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;

public class FilterFunction<T> implements IteratorToIteratorFunction<T> {

	private final Predicate<T> predicate;
	
	public FilterFunction(Predicate<T> predicate) {
		this.predicate = predicate;
	}

	@Override
	public Iterator<T> apply(Iterator<T> input) {
		return new FilteringIteratorWrapper<T>(input, predicate);
	}

}
