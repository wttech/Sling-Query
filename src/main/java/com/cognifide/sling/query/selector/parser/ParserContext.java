package com.cognifide.sling.query.selector.parser;

import java.util.ArrayList;
import java.util.List;

public class ParserContext {
	private final List<String> attributes = new ArrayList<String>();

	private final List<String> functions = new ArrayList<String>();

	private State state = State.START;

	private StringBuilder builder = new StringBuilder();

	private String resourceType;

	int parentCount = 0;

	int squareParentCount = 0;

	public List<String> getAttributes() {
		return attributes;
	}

	public List<String> getFunctions() {
		return functions;
	}

	public String getResourceType() {
		return resourceType;
	}

	public State getState() {
		return state;
	}

	void setResourceType() {
		resourceType = builder.toString();
		builder = new StringBuilder();
	}

	void addAttribute() {
		attributes.add(builder.toString());
		builder = new StringBuilder();
	}

	void addFunction() {
		functions.add(builder.toString());
		builder = new StringBuilder();
	}

	void setState(State state) {
		this.state = state;
	}

	void append(char c) {
		builder.append(c);
	}

}
