package com.cognifide.sling.query.selector;

public class OptionalElement<T> {
	private final T source;

	private final T element;

	public OptionalElement(T element, T source) {
		this.element = element;
		this.source = source;
	}

	public T getSource() {
		return source;
	}

	public T getElement() {
		return element;
	}

	public boolean isEmpty() {
		return element == null;
	}
}
