package com.cognifide.sling.query.iterator;

import java.util.Iterator;

public class SliceIterator<T> extends AbstractIterator<T> {

	private final Iterator<T> iterator;

	private int current;

	private int from;

	private Integer to;

	public SliceIterator(Iterator<T> iterator, int from, int to) {
		this.iterator = iterator;
		this.current = 0;
		this.from = from;
		this.to = to;
	}

	public SliceIterator(Iterator<T> iterator, int from) {
		this.iterator = iterator;
		this.current = 0;
		this.from = from;
		this.to = null;
	}

	@Override
	protected T getElement() {
		if (to != null && current > to) {
			return null;
		}
		T element;
		do {
			if (!iterator.hasNext()) {
				return null;
			}
			element = iterator.next();
		} while (current++ < from);
		return element;
	}
}