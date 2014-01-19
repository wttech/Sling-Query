package com.cognifide.sling.query.selector;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.cognifide.sling.query.api.Function;

class ParsedSelectorFunction {
	private final SelectorFunction function;

	private final String argument;

	public ParsedSelectorFunction(String functionId, String argument) {
		this.function = SelectorFunction.valueOf(functionId.toUpperCase());
		this.argument = argument;
	}

	public Function<?, ?> function() {
		return function.getFunction(argument);
	}

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
		ParsedSelectorFunction rhs = (ParsedSelectorFunction) obj;
		return new EqualsBuilder().append(function, rhs.function).append(argument, rhs.argument).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(function).append(argument).toHashCode();
	}
}