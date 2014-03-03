package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.Option;

public class OptionStrippingIterator<T> implements Iterator<T> {

	private final Iterator<Option<T>> iterator;

	public OptionStrippingIterator(Iterator<Option<T>> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public T next() {
		return iterator.next().getElement();
	}

	@Override
	public void remove() {
		iterator.remove();
	}
}
