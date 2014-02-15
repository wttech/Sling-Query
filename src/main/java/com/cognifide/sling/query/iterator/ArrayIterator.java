package com.cognifide.sling.query.iterator;

public class ArrayIterator<T> extends AbstractIterator<T> {

	private final T[] elements;

	private int index = 0;

	public ArrayIterator(T... elements) {
		this.elements = elements;
	}

	@Override
	protected T getElement() {
		if (index >= elements.length) {
			return null;
		} else {
			return elements[index++];
		}
	}

}
