package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.ArrayIterator;

public class LastFunction<T> implements IteratorToIteratorFunction<T> {

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> apply(Iterator<T> input) {
		T lastElement = null;
		while (input.hasNext()) {
			lastElement = input.next();
		}
		return new ArrayIterator<T>(lastElement);
	}

}
