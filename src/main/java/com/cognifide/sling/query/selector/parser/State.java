package com.cognifide.sling.query.selector.parser;

public enum State {
	START {
		@Override
		public void process(ParserContext context, char c) {
			if (c == '/') {
				context.setState(State.RESOURCE_TYPE_WITH_SLASHES);
				context.append(c);
			} else if (c == '[') {
				context.increaseSquareParentheses();
				context.setState(State.ATTRIBUTE);
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
				context.increaseSquareParentheses();
				context.setState(State.ATTRIBUTE);
			} else if (c == ':') {
				context.setState(State.FUNCTION);
			} else if (c == ' ') {
				context.finishSelectorSegment();
				context.setState(START);
			} else if (c == 0) {
				context.finishSelectorSegment();
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
			} else if (c == ' ') {
				context.setResourceType();
				context.finishSelectorSegment();
				context.setState(START);
			} else if (c == 0) {
				context.setResourceType();
				context.finishSelectorSegment();
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
			} else if (c == ' ') {
				context.setResourceType();
				context.finishSelectorSegment();
				context.setState(START);
			} else if (c == 0) {
				context.setResourceType();
				context.finishSelectorSegment();
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
					context.setState(State.IDLE);
					context.addAttribute();
				}
			} else if (c == 0) {
				context.addAttribute();
				context.finishSelectorSegment();
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
			} else if (c == ' ') {
				context.addFunction();
				context.finishSelectorSegment();
				context.setState(START);
			} else if (c == 0) {
				context.addFunction();
				context.finishSelectorSegment();
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
}
