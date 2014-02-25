package com.cognifide.sling.query.selector.parser;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Modifier {

	private final String name;

	private final String argument;

	public Modifier(String name, String argument) {
		this.name = name;
		this.argument = argument;
	}

	public String getName() {
		return name;
	}

	public String getArgument() {
		return argument;
	}

	@Override
	public String toString() {
		return String.format("Modifier[%s,%s]", name, argument);
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
		Modifier rhs = (Modifier) obj;
		return new EqualsBuilder().append(name, rhs.name).append(argument, rhs.argument)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(argument).toHashCode();
	}
}