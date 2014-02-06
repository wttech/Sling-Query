package com.cognifide.sling.query.selector.parser;

import java.util.ArrayList;
import java.util.List;

import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.predicate.PropertyPredicate;

public class ParserContext {
	private final SearchStrategy strategy;

	private final List<SelectorSegment> segments = new ArrayList<SelectorSegment>();

	private final List<PropertyPredicate> attributes = new ArrayList<PropertyPredicate>();

	private final List<SelectorFunction> functions = new ArrayList<SelectorFunction>();

	private char hierarchyOperator;

	private State state = State.START;

	private StringBuilder builder = new StringBuilder();

	private String resourceType;

	private String resourceName;

	private String attributeKey;

	private String attributeOperator;

	private String attributeValue;

	private String currentFunctionName;

	private int parenthesesCount = 0;

	ParserContext(SearchStrategy strategy) {
		this.strategy = strategy;
	}

	List<PropertyPredicate> getAttributes() {
		return attributes;
	}

	List<SelectorFunction> getFunctions() {
		return functions;
	}

	String getResourceType() {
		return resourceType;
	}

	String getResourceName() {
		return resourceName;
	}

	char getHierarchyOperator() {
		return hierarchyOperator;
	}

	public State getState() {
		return state;
	}

	void increaseParentheses() {
		parenthesesCount++;
	}

	int decreaseParentheses() {
		return --parenthesesCount;
	}

	void setResourceType() {
		resourceType = builder.toString();
		builder = new StringBuilder();
	}

	void setResourceName() {
		resourceName = builder.toString();
		builder = new StringBuilder();
	}

	void setAttributeKey() {
		attributeKey = builder.toString();
		builder = new StringBuilder();
	}

	void setAttributeOperator() {
		attributeOperator = builder.toString();
		builder = new StringBuilder();
	}

	void setAttributeValue() {
		attributeValue = builder.toString();
		builder = new StringBuilder();
	}

	void addAttribute() {
		attributes.add(new PropertyPredicate(attributeKey, attributeOperator, attributeValue));
		attributeKey = null;
		attributeOperator = null;
		attributeValue = null;
	}

	void setFunctionName() {
		currentFunctionName = builder.toString();
		builder = new StringBuilder();
	}

	void addFunction() {
		SelectorFunction function;
		if (currentFunctionName == null) {
			function = new SelectorFunction(builder.toString(), null);
		} else {
			function = new SelectorFunction(currentFunctionName, builder.toString());
			currentFunctionName = null;
		}
		functions.add(function);
		builder = new StringBuilder();
	}

	void setState(State state) {
		this.state = state;
	}

	void setHierarchyOperator(char hierarchyOperator) {
		this.hierarchyOperator = hierarchyOperator;
	}

	void finishSelectorSegment() {
		segments.add(new SelectorSegment(this, segments.isEmpty(), strategy));
		attributes.clear();
		functions.clear();
		hierarchyOperator = 0;
		resourceType = null;
	}

	void append(char c) {
		builder.append(c);
	}

	public List<SelectorSegment> getSegments() {
		return segments;
	}

}
