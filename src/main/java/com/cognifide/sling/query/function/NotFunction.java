package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.ExcludingIterator;

public class NotFunction<T> implements IteratorToIteratorFunction<T> {

	private final IteratorToIteratorFunction<T> function;

	public NotFunction(IteratorToIteratorFunction<T> function) {
		this.function = function;
	}

	@Override
	public Iterator<T> apply(Iterator<T> input) {
		return new ExcludingIterator<T>(input, function);
	}
}
