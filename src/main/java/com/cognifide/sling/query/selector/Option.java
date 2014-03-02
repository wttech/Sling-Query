package com.cognifide.sling.query.selector;

public class Option<T> {
	private final T element;

	public Option(T element) {
		this.element = element;
	}

	public Option() {
		this.element = null;
	}

	public T getElement() {
		return element;
	}

	public boolean isEmpty() {
		return element == null;
	}
}
