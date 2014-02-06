package com.cognifide.sling.query.selector.parser;

import com.cognifide.sling.query.api.SearchStrategy;

public final class SelectorParser {

	private SelectorParser() {
	}

	public static ParserContext parse(String selector, SearchStrategy strategy) {
		ParserContext context = new ParserContext(strategy);
		for (char c : selector.toCharArray()) {
			context.getState().process(context, c);
		}
		context.getState().process(context, (char) 0);
		return context;
	}
}
