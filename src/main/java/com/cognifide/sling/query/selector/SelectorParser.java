package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.List;

public class SelectorParser {
	private enum State {
		START, RESOURCE_TYPE, RESOURCE_TYPE_WITH_SLASHES, ATTRIBUTE, FUNCTION, FUNCTION_ARGUMENT
	}

	private String resourceType;

	private List<String> attributes = new ArrayList<String>();

	private List<String> functions = new ArrayList<String>();

	public void parse(String selectorString) {
		State state = State.START;
		StringBuilder builder = new StringBuilder();
		for (char c : selectorString.toCharArray()) {
			if (state == State.START && Character.isAlphabetic(c)) {
				state = State.RESOURCE_TYPE;
			} else if (state == State.RESOURCE_TYPE && c == '/') {
				state = State.RESOURCE_TYPE_WITH_SLASHES;
			} else if (state == State.RESOURCE_TYPE && c == '[') {
				state = State.ATTRIBUTE;
				resourceType = builder.toString();
				builder = new StringBuilder();
			} else if (state == State.RESOURCE_TYPE && c == ':') {
				state = State.RESOURCE_TYPE_WITH_SLASHES;
			} else if (state == State.RESOURCE_TYPE_WITH_SLASHES && c == '[') {
				state = State.ATTRIBUTE;
				resourceType = builder.toString();
				builder = new StringBuilder();
			} else if (state == State.START && c == '[') {
				state = State.ATTRIBUTE;
			} else if (state == State.RESOURCE_TYPE_WITH_SLASHES && c == ':') {
				state = State.FUNCTION;
				resourceType = builder.toString();
				builder = new StringBuilder();
			} else if (state == State.ATTRIBUTE && c == ']') {
				state = State.START;
				attributes.add(builder.append(']').toString());
				builder = new StringBuilder();
				continue;
			} else if (state == State.START && c == ':') {
				state = State.FUNCTION;
			} else if (state == State.FUNCTION && c == ':') {
				functions.add(builder.toString());
				builder = new StringBuilder();
			} else if (state == State.FUNCTION && c == '(') {
				state = State.FUNCTION_ARGUMENT;
			} else if (state == State.FUNCTION_ARGUMENT && c == ')') {
				state = State.FUNCTION;
			}
			builder.append(c);
		}
		if (state == State.RESOURCE_TYPE || state == State.RESOURCE_TYPE_WITH_SLASHES) {
			resourceType = builder.toString();
		} else if (state == State.FUNCTION) {
			functions.add(builder.toString());
		}
	}

	public String getResourceType() {
		return resourceType;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public List<String> getFunctions() {
		return functions;
	}
}
