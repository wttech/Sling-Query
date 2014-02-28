package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.selector.OptionalElement;

public class OptionalElementIterator<T> extends AbstractIterator<OptionalElement<T>> {

	private final Iterator<T> iterator;

	private final T source;

	private boolean empty;

	public OptionalElementIterator(Iterator<T> iterator, T source) {
		this.iterator = iterator;
		this.source = source;
		this.empty = !iterator.hasNext();
	}

	@Override
	protected OptionalElement<T> getElement() {
		if (iterator.hasNext()) {
			return new OptionalElement<T>(iterator.next(), source);
		}
		if (empty) {
			empty = false;
			return new OptionalElement<T>(null, source);
		}
		return null;
	}

}
