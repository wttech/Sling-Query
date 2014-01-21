package com.cognifide.sling.query.selector.parser;

import java.util.ArrayList;
import java.util.List;

import com.cognifide.sling.query.predicate.PropertyPredicate;

public class ParserContext {
	private final List<PropertyPredicate> attributes = new ArrayList<PropertyPredicate>();

	private final List<SelectorFunction> functions = new ArrayList<SelectorFunction>();

	private State state = State.START;

	private StringBuilder builder = new StringBuilder();

	private String resourceType;

	private String currentFunctionName;

	private int parenthesesCount = 0;

	private int squareParenthesesCount = 0;

	public List<PropertyPredicate> getAttributes() {
		return attributes;
	}

	public List<SelectorFunction> getFunctions() {
		return functions;
	}

	public String getResourceType() {
		return resourceType;
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

	void increaseSquareParentheses() {
		squareParenthesesCount++;
	}

	int decreaseSquareParentheses() {
		return --squareParenthesesCount;
	}

	void setResourceType() {
		resourceType = builder.toString();
		builder = new StringBuilder();
	}

	void addAttribute() {
		attributes.add(new PropertyPredicate(builder.toString()));
		builder = new StringBuilder();
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

	void append(char c) {
		builder.append(c);
	}

}
