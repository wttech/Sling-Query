package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.List;

public class SelectorParser {
	private final List<String> attributes = new ArrayList<String>();

	private final List<String> functions = new ArrayList<String>();

	private State state;

	private StringBuilder builder;

	private String resourceType;

	private int parentCount = 0;

	public void parse(String selectorString) {
		state = State.START;
		builder = new StringBuilder();
		for (char c : selectorString.toCharArray()) {
			state.process(this, c);
		}
		state.process(this, (char) 0);
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

	private enum State {
		START {
			@Override
			protected void process(SelectorParser context, char c) {
				if (Character.isAlphabetic(c)) {
					context.state = State.RESOURCE_TYPE;
					context.builder.append(c);
				} else if (c == '[') {
					context.state = State.ATTRIBUTE;
				} else if (c == ':') {
					context.state = State.FUNCTION;
				} else {
					context.builder.append(c);
				}
			}
		},
		RESOURCE_TYPE {
			@Override
			protected void process(SelectorParser context, char c) {
				if (c == '/') {
					context.state = State.RESOURCE_TYPE_WITH_SLASHES;
					context.builder.append(c);
				} else if (c == '[') {
					context.state = State.ATTRIBUTE;
					context.resourceType = context.builder.toString();
					context.builder = new StringBuilder();
				} else if (c == ':') {
					context.state = State.RESOURCE_TYPE_WITH_SLASHES;
					context.builder.append(c);
				} else if (c == 0) {
					context.resourceType = context.builder.toString();
				} else {
					context.builder.append(c);
				}
			}
		},
		RESOURCE_TYPE_WITH_SLASHES {
			@Override
			protected void process(SelectorParser context, char c) {
				if (c == '[') {
					context.state = State.ATTRIBUTE;
					context.resourceType = context.builder.toString();
					context.builder = new StringBuilder();
				} else if (c == ':') {
					context.state = State.FUNCTION;
					context.resourceType = context.builder.toString();
					context.builder = new StringBuilder();
				} else if (c == 0) {
					context.resourceType = context.builder.toString();
				} else {
					context.builder.append(c);
				}
			}
		},
		ATTRIBUTE {
			@Override
			protected void process(SelectorParser context, char c) {
				if (c == ']') {
					context.state = State.START;
					context.attributes.add(context.builder.toString());
					context.builder = new StringBuilder();
				} else if (c == 0) {
					context.attributes.add(context.builder.toString());
				} else {
					context.builder.append(c);
				}
			}
		},
		FUNCTION {
			@Override
			protected void process(SelectorParser context, char c) {
				if (c == ':') {
					context.functions.add(context.builder.toString());
					context.builder = new StringBuilder();
				} else if (c == '(') {
					context.state = State.FUNCTION_ARGUMENT;
					context.parentCount++;
					context.builder.append(c);
				} else if (c == 0) {
					context.functions.add(context.builder.toString());
				} else {
					context.builder.append(c);
				}
			}
		},
		FUNCTION_ARGUMENT {
			@Override
			protected void process(SelectorParser context, char c) {
				if (c == ')') {
					if (--context.parentCount == 0) {
						context.state = State.FUNCTION;
					}
				} else if (c == '(') {
					context.parentCount++;
				}
				context.builder.append(c);
			}
		};
		protected abstract void process(SelectorParser context, char c);
	}
}
