package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.ExcludingIterator;
import com.cognifide.sling.query.selector.Selector;

public class NotFunction<T> implements IteratorToIteratorFunction<T> {

	private final Selector<T> selector;

	public NotFunction(Selector<T> selector) {
		this.selector = selector;
	}

	@Override
	public Iterator<T> apply(Iterator<T> input) {
		return new ExcludingIterator<T>(input, selector);
	}
}
