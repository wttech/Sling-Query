package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.selector.Option;

public class EmptyElementFilter<T> extends AbstractIterator<T> {

	private final Iterator<Option<T>> input;

	public EmptyElementFilter(Iterator<Option<T>> input) {
		this.input = input;
	}

	@Override
	protected T getElement() {
		while (input.hasNext()) {
			Option<T> element = input.next();
			if (!element.isEmpty()) {
				return element.getElement();
			}
		}
		return null;
	}

}
