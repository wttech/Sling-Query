package com.cognifide.sling.query.resource;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.predicate.SelectorOperator;
import com.cognifide.sling.query.selector.parser.Attribute;

public class ResourcePropertyPredicate implements Predicate<Resource> {
	private final String key;

	private final String value;

	private final SelectorOperator operator;

	public ResourcePropertyPredicate(Attribute attribute) {
		this.key = attribute.getKey();
		this.value = attribute.getValue();
		this.operator = SelectorOperator.getSelectorOperator(attribute.getOperator());
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
}