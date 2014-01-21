package com.cognifide.sling.query.selector.parser;

public enum State {
	START {
		@Override
		public void process(ParserContext context, char c) {
			if (Character.isAlphabetic(c)) {
				context.setState(State.RESOURCE_TYPE);
				context.append(c);
			} else if (c == '[') {
				context.increaseSquareParentheses();
				context.setState(State.ATTRIBUTE);
			} else if (c == ':') {
				context.setState(State.FUNCTION);
			} else {
				context.append(c);
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
				context.increaseSquareParentheses();
				context.setState(State.ATTRIBUTE);
				context.setResourceType();
			} else if (c == ':') {
				context.setState(State.RESOURCE_TYPE_WITH_SLASHES);
				context.append(c);
			} else if (c == 0) {
				context.setResourceType();
			} else {
				context.append(c);
			}
		}
	},
	RESOURCE_TYPE_WITH_SLASHES {
		@Override
		public void process(ParserContext context, char c) {
			if (c == '[') {
				context.increaseSquareParentheses();
				context.setState(State.ATTRIBUTE);
				context.setResourceType();
			} else if (c == ':') {
				context.setState(State.FUNCTION);
				context.setResourceType();
			} else if (c == 0) {
				context.setResourceType();
			} else {
				context.append(c);
			}
		}
	},
	ATTRIBUTE {
		@Override
		public void process(ParserContext context, char c) {
			if (c == ']') {
				if (context.decreaseSquareParentheses() == 0) {
					context.setState(State.START);
					context.addAttribute();
				}
			} else if (c == 0) {
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
			} else if (c == 0) {
				context.addFunction();
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
					context.setState(START);
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
}
