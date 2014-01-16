package com.cognifide.sling.query.predicate;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.ResourcePredicate;

public class PropertyPredicate implements ResourcePredicate {
	private final String path;

	private final String value;

	public PropertyPredicate(String path, String value) {
		this.path = path;
		this.value = value;
	}

	@Override
	public boolean accepts(Resource resource) {
		return value.equals(resource.getChild(path).adaptTo(String.class));
	}

	public String toString() {
		return String.format("PropertyPredicate[%s=%s]", path, value);
	}
}