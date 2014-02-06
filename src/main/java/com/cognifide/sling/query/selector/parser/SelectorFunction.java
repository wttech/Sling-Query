package com.cognifide.sling.query.selector.parser;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.selector.FunctionType;

public class SelectorFunction {
	private final FunctionType function;

	private final String argument;

	public SelectorFunction(String functionName, String argument) {
		this.function = FunctionType.valueOf(functionName.toUpperCase());
		this.argument = argument;
	}

	public Function<?, ?> function(SearchStrategy strategy) {
		return function.getFunction(argument, strategy);
	}

	@Override
	public String toString() {
		return String.format("Function[%s,%s]", function.name(), argument);
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
		SelectorFunction rhs = (SelectorFunction) obj;
		return new EqualsBuilder().append(function, rhs.function).append(argument, rhs.argument).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(function).append(argument).toHashCode();
	}
}