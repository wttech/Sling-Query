package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.api.Function;

public class FunctionIterator<T> extends AbstractIterator<T> {

	private final Function<T, Iterator<T>> function;

	private final Iterator<T> parentIterator;

	private Iterator<T> currentIterator;

	public FunctionIterator(Function<T, Iterator<T>> function, Iterator<T> parentIterator) {
		this.function = function;
		this.parentIterator = parentIterator;
	}

	@Override
	protected T getElement() {
		if (currentIterator != null && currentIterator.hasNext()) {
			return currentIterator.next();
		}
		while (parentIterator.hasNext()) {
			T parentElement = parentIterator.next();
			currentIterator = function.apply(parentElement);
			if (currentIterator.hasNext()) {
				return currentIterator.next();
			}
		}
		return null;
	}
}
