package com.cognifide.sling.query.iterator;

import java.util.Iterator;

public class LastIterator<T> extends AbstractIterator<T> {

	private final Iterator<T> iterator;

	public LastIterator(Iterator<T> iterator) {
		this.iterator = iterator;
	}

	@Override
	protected T getElement() {
		T element = null;
		while (iterator.hasNext()) {
			element = iterator.next();
		}
		return element;
	}
}
