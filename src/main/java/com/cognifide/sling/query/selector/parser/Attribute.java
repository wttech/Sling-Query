package com.cognifide.sling.query.selector.parser;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Attribute {
	private final String key;

	private final String operator;

	private final String value;

	public Attribute(String key, String operator, String value) {
		this.key = key;
		this.operator = operator;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getOperator() {
		return operator;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		return String.format("Attribute[%s %s %s]", key, operator, value);
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
		Attribute rhs = (Attribute) obj;
		return new EqualsBuilder().append(key, rhs.key).append(value, rhs.value).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(key).append(value).toHashCode();
	}
}
