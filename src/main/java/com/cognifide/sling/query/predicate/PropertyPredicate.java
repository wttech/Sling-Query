package com.cognifide.sling.query.predicate;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;

public class PropertyPredicate implements ResourcePredicate {
	private final String key;

	private final String value;

	private final SelectorOperator operator;

	public PropertyPredicate(String key, String operator, String value) {
		this.key = key;
		this.value = value;
		this.operator = SelectorOperator.getSelectorOperator(operator);
	}

	@Override
	public boolean accepts(Resource resource) {
		Resource property = resource.getChild(key);
		if (property == null) {
			return false;
		} else if (value == null) {
			return true;
		} else {
			return operator.accepts(property.adaptTo(String.class), value);
		}
	}

	public String toJcrString() {
		return operator.getJcrQueryFragment(key, StringUtils.replace(value, "'", "''"));
	}

	public String toString() {
		return String.format("PropertyPredicate[%s %s %s]", key, operator, value);
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
		PropertyPredicate rhs = (PropertyPredicate) obj;
		return new EqualsBuilder().append(key, rhs.key).append(value, rhs.value).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(key).append(value).toHashCode();
	}
}