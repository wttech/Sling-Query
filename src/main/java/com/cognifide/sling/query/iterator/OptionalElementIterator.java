package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.selector.Option;

public class OptionalElementIterator<T> extends AbstractIterator<Option<T>> {

	private final Iterator<T> iterator;

	private boolean empty;

	public OptionalElementIterator(Iterator<T> iterator) {
		this.iterator = iterator;
		this.empty = !iterator.hasNext();
	}

	@Override
	protected Option<T> getElement() {
		if (iterator.hasNext()) {
			T element = iterator.next();
			return new Option<T>(element);
		}
		if (empty) {
			empty = false;
			return new Option<T>();
		}
		return null;
	}

}
