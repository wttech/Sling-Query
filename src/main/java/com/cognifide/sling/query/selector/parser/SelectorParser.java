package com.cognifide.sling.query.selector.parser;

import com.cognifide.sling.query.TreeStructureProvider;
import com.cognifide.sling.query.api.SearchStrategy;

public final class SelectorParser {

	private SelectorParser() {
	}

	public static <T> ParserContext<T> parse(String selector, SearchStrategy strategy,
			TreeStructureProvider<T> provider) {
		ParserContext<T> context = new ParserContext<T>(strategy, provider);
		for (char c : selector.toCharArray()) {
			context.getState().process(context, c);
		}
		context.getState().process(context, (char) 0);
		return context;
	}
}
