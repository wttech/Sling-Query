package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.ElementToIteratorFunction;

public class FunctionIterator<T> extends AbstractIterator<T> {

	private final ElementToIteratorFunction<T> function;

	private final Iterator<T> parentIterator;

	private Iterator<T> currentIterator;

	public FunctionIterator(ElementToIteratorFunction<T> function, Iterator<T> parentIterator) {
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
