package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.Option;

public class OptionDecoratingIterator<T> implements Iterator<Option<T>> {

	private final Iterator<T> iterator;

	private int index;

	public OptionDecoratingIterator(Iterator<T> iterator) {
		this.iterator = iterator;
		this.index = 0;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Option<T> next() {
		return Option.of(iterator.next(), index++);
	}

	@Override
	public void remove() {
		iterator.remove();
	}

}
