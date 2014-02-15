package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.api.Predicate;

public class FilteringIteratorWrapper<T> extends AbstractIterator<T> {

	private final Iterator<T> iterator;

	private final Predicate<T> predicate;

	public FilteringIteratorWrapper(Iterator<T> iterator, Predicate<T> predicate) {
		this.iterator = iterator;
		this.predicate = predicate;
	}

	@Override
	protected T getElement() {
		while (iterator.hasNext()) {
			T element = iterator.next();
			if (predicate.accepts(element)) {
				return element;
			}
		}
		return null;
	}
}
