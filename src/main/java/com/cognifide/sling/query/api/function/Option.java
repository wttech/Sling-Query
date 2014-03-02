package com.cognifide.sling.query.api.function;

public class Option<T> {
	private final T element;

	private Option(T element) {
		this.element = element;
	}

	public static <T> Option<T> of(T element) {
		return new Option<T>(element);
	}

	public static <T> Option<T> empty() {
		return new Option<T>(null);
	}

	public T getElement() {
		return element;
	}

	public boolean isEmpty() {
		return element == null;
	}
}
