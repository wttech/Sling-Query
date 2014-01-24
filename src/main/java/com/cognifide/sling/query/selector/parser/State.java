package com.cognifide.sling.query.selector.parser;

import org.apache.commons.lang.ArrayUtils;

public enum State {
	START {
		@Override
		public void process(ParserContext context, char c) {
			if (c == '/') {
				context.setState(State.RESOURCE_TYPE_WITH_SLASHES);
				context.append(c);
			} else if (c == '[') {
				context.setState(State.ATTRIBUTE_KEY);
			} else if (c == ':') {
				context.setState(State.FUNCTION);
			} else if (c == '>' || c == '+' || c == '~') {
				context.setHierarchyOperator(c);
			} else if (c != ' ') {
				context.setState(State.RESOURCE_TYPE);
				context.append(c);
			}
		}
	},
	IDLE {
		@Override
		public void process(ParserContext context, char c) {
			if (c == '[') {
				context.setState(State.ATTRIBUTE_KEY);
			} else if (c == ':') {
				context.setState(State.FUNCTION);
			} else if (c == ' ' || c == 0) {
				context.finishSelectorSegment();
				context.setState(START);
			}
		}
	},
	RESOURCE_TYPE {
		@Override
		public void process(ParserContext context, char c) {
			if (c == '/') {
				context.setState(State.RESOURCE_TYPE_WITH_SLASHES);
				context.append(c);
			} else if (c == '[') {
				context.setState(State.ATTRIBUTE_KEY);
				context.setResourceType();
			} else if (c == ':') {
				context.setState(State.RESOURCE_TYPE_WITH_SLASHES);
				context.append(c);
			} else if (c == ' ' || c == 0) {
				context.setResourceType();
				context.finishSelectorSegment();
				context.setState(START);
			} else {
				context.append(c);
			}
		}
	},
	RESOURCE_TYPE_WITH_SLASHES {
		@Override
		public void process(ParserContext context, char c) {
			if (c == '[') {
				context.setState(State.ATTRIBUTE_KEY);
				context.setResourceType();
			} else if (c == ':') {
				context.setState(State.FUNCTION);
				context.setResourceType();
			} else if (c == ' ' || c == 0) {
				context.setResourceType();
				context.finishSelectorSegment();
				context.setState(START);
			} else {
				context.append(c);
			}
		}
	},
	ATTRIBUTE_KEY {
		@Override
		public void process(ParserContext context, char c) {
			if (c == ']') {
				context.setAttributeKey();
				context.addAttribute();
				context.setState(State.IDLE);
			} else if (ArrayUtils.contains(OPERATORS, c)) {
				context.setAttributeKey();
				context.setState(State.ATTRIBUTE_OPERATOR);
				context.append(c);
			} else {
				context.append(c);
			}
		}
	},
	ATTRIBUTE_OPERATOR {
		@Override
		public void process(ParserContext context, char c) {
			if (!ArrayUtils.contains(OPERATORS, c)) {
				context.setAttributeOperator();
				context.append(c);
				context.setState(ATTRIBUTE_VALUE);
			} else {
				context.append(c);
			}
		}
	},
	ATTRIBUTE_VALUE {
		@Override
		public void process(ParserContext context, char c) {
			if (c == ']') {
				context.setState(State.IDLE);
				context.setAttributeValue();
				context.addAttribute();
			} else {
				context.append(c);
			}
		}
	},
	FUNCTION {
		@Override
		public void process(ParserContext context, char c) {
			if (c == ':') {
				context.addFunction();
			} else if (c == '(') {
				context.setFunctionName();
				context.setState(State.FUNCTION_ARGUMENT);
				context.increaseParentheses();
			} else if (c == ' ' || c == 0) {
				context.addFunction();
				context.finishSelectorSegment();
				context.setState(START);
			} else {
				context.append(c);
			}
		}
	},
	FUNCTION_ARGUMENT {
		@Override
		public void process(ParserContext context, char c) {
			if (c == ')') {
				if (context.decreaseParentheses() == 0) {
					context.addFunction();
					context.setState(IDLE);
				} else {
					context.append(c);
				}
			} else if (c == '(') {
				context.increaseParentheses();
				context.append(c);
			} else {
				context.append(c);
			}
		}
	};
	public abstract void process(ParserContext context, char c);

	private static final char[] OPERATORS = "*~$!^=".toCharArray();
}
