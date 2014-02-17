package com.cognifide.sling.query.iterator;

import java.util.Iterator;

public class ExcludingIterator<T> extends AbstractIterator<T> {

	private final Iterator<T> iterator;

	private final Iterator<T> toExclude;

	private T nextToExclude;

	public ExcludingIterator(Iterator<T> iterator, Iterator<T> toExclude) {
		this.iterator = iterator;
		this.toExclude = toExclude;
	}

	@Override
	protected T getElement() {
		T element = null;
		while (iterator.hasNext() && element == null) {
			if (nextToExclude == null && toExclude.hasNext()) {
				nextToExclude = toExclude.next();
			}
			T candidate = iterator.next();
			if (candidate.equals(nextToExclude)) {
				nextToExclude = null;
			} else {
				element = candidate;
			}
		}
		return element;
	}

}
