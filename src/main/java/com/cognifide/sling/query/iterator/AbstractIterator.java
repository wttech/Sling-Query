package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractIterator<T> implements Iterator<T> {

	private T currentElement;

	@Override
	public boolean hasNext() {
		if (currentElement == null) {
			currentElement = getElement();
		}
		return currentElement != null;
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		T result = currentElement;
		currentElement = null;
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	protected abstract T getElement();
}
