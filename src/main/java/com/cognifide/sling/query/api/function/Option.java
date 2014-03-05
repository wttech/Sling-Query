package com.cognifide.sling.query.api.function;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Option<T> {
	private final T element;

	private final int argumentId;

	private Option(T element, int argumentId) {
		this.element = element;
		this.argumentId = argumentId;
	}

	public static <T> Option<T> of(T element, int argumentId) {
		return new Option<T>(element, argumentId);
	}

	public static <T> Option<T> empty(int argumentId) {
		return new Option<T>(null, argumentId);
	}

	public int getArgumentId() {
		return argumentId;
	}

	public T getElement() {
		return element;
	}

	public boolean isEmpty() {
		return element == null;
	}

	public String toString() {
		if (isEmpty()) {
			return "Option[-]";
		} else {
			return String.format("Option[%s]", element.toString());
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Option<?> rhs = (Option<?>) obj;
		return new EqualsBuilder().append(element, rhs.element).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(element).toHashCode();
	}
}
