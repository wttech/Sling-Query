package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.LastIterator;

public class LastFunction<T> implements IteratorToIteratorFunction<T> {

	@Override
	public Iterator<T> apply(Iterator<T> input) {
		return new LastIterator<T>(input);
	}

}
