package com.cognifide.sling.query.iterator;

import java.util.Iterator;

public class ArrayIterator<T> extends AbstractIterator<T> {

	private final T[] elements;

	private int index = 0;

	public ArrayIterator(T... elements) {
		this.elements = elements;
	}

	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> getEmptyIterator() {
		return new ArrayIterator<T>();
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
