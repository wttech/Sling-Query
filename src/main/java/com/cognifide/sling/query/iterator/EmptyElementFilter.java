package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.Option;

public class EmptyElementFilter<T> extends AbstractIterator<Option<T>> {

	private final Iterator<Option<T>> input;

	public EmptyElementFilter(Iterator<Option<T>> input) {
		this.input = input;
	}

	@Override
	protected Option<T> getElement() {
		while (input.hasNext()) {
			Option<T> element = input.next();
			if (!element.isEmpty()) {
				return element;
			}
		}
		return null;
	}

}
