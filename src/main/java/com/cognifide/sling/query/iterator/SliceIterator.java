package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.selector.Option;

public class SliceIterator<T> extends AbstractIterator<Option<T>> {

	private final Iterator<Option<T>> iterator;

	private final int from;

	private final int to;

	private int current;

	public SliceIterator(Iterator<Option<T>> iterator, int from, int to) {
		this.iterator = iterator;
		this.current = -1;
		this.from = from;
		this.to = to;
	}

	public SliceIterator(Iterator<Option<T>> iterator, int from) {
		this(iterator, from, Integer.MAX_VALUE);
	}

	@Override
	protected Option<T> getElement() {
		if (iterator.hasNext()) {
			Option<T> element = iterator.next();
			if (element.isEmpty()) {
				return element;
			}
			if (++current >= from && current <= to) {
				return element;
			} else {
				return new Option<T>();
			}
		}
		return null;
	}
}