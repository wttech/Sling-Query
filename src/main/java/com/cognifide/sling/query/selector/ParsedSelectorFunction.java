package com.cognifide.sling.query.selector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.cognifide.sling.query.api.Function;

class ParsedSelectorFunction {
	private final SelectorFunction function;

	private final String argument;

	public ParsedSelectorFunction(String functionName, String argument) {
		this.function = SelectorFunction.valueOf(functionName.toUpperCase());
		this.argument = argument;
	}

	public ParsedSelectorFunction(String functionString) {
		String functionName = functionString;
		String rawArgument = null;
		if (functionString.contains("(")) {
			functionName = StringUtils.substringBefore(functionString, "(");
			rawArgument = StringUtils.substringAfter(functionString, "(");
			rawArgument = StringUtils.substringBeforeLast(rawArgument, ")");
		}
		function = SelectorFunction.valueOf(functionName.toUpperCase());
		argument = rawArgument;
	}

	public Function<?, ?> function() {
		return function.getFunction(argument);
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
		ParsedSelectorFunction rhs = (ParsedSelectorFunction) obj;
		return new EqualsBuilder().append(function, rhs.function).append(argument, rhs.argument).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(function).append(argument).toHashCode();
	}
}