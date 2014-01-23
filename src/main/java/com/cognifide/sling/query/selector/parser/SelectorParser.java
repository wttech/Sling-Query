package com.cognifide.sling.query.selector.parser;

public final class SelectorParser {

	private SelectorParser() {
	}

	public static ParserContext parse(String selector) {
		ParserContext context = new ParserContext();
		for (char c : selector.toCharArray()) {
			context.getState().process(context, c);
		}
		context.getState().process(context, (char) 0);
		return context;
	}
}
