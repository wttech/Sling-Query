package com.cognifide.sling.query.iterator;

import java.util.Iterator;

public class MergingIterator<T> extends AbstractIterator<T> {

	private final Iterator<T>[] iterators;

	private int index = 0;

	public MergingIterator(Iterator<T>... iterators) {
		this.iterators = iterators;
	}

	@Override
	protected T getElement() {
		while (index < iterators.length) {
			if (iterators[index].hasNext()) {
				return iterators[index].next();
			} else {
				index++;
			}
		}
		return null;
	}

}
