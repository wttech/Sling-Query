package com.cognifide.sling.query.predicate;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;

public class PropertyPredicate implements ResourcePredicate {
	private final String path;

	private final String value;

	public PropertyPredicate(String path, String value) {
		this.path = path;
		this.value = value;
	}

	@Override
	public boolean accepts(Resource resource) {
		Resource property = resource.getChild(path);
		if (property == null) {
			return false;
		} else {
			return value.equals(property.adaptTo(String.class));
		}
	}

	public String toString() {
		return String.format("PropertyPredicate[%s=%s]", path, value);
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
		return new EqualsBuilder().append(path, rhs.path).append(value, rhs.value).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(path).append(value).toHashCode();
	}
}