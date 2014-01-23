package com.cognifide.sling.query.selector.parser;

import java.util.ArrayList;
import java.util.List;

import com.cognifide.sling.query.predicate.PropertyPredicate;

public class SelectorSegment {
	private final String resourceType;

	private final List<PropertyPredicate> attributes;

	private final List<SelectorFunction> functions;

	private final char hierarchyOperator;

	public SelectorSegment(ParserContext context) {
		this.resourceType = context.getResourceType();
		this.attributes = new ArrayList<PropertyPredicate>(context.getAttributes());
		this.functions = new ArrayList<SelectorFunction>(context.getFunctions());
		this.hierarchyOperator = context.getHierarchyOperator();
	}

	public String getResourceType() {
		return resourceType;
	}

	public List<PropertyPredicate> getAttributes() {
		return attributes;
	}

	public List<SelectorFunction> getFunctions() {
		return functions;
	}

	public char getHierarchyOperator() {
		return hierarchyOperator;
	}
}
