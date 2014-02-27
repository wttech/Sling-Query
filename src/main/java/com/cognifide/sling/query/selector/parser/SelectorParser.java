package com.cognifide.sling.query.selector.parser;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public final class SelectorParser {

	private SelectorParser() {
	}

	public static List<Selector> parse(String selector) {
		if (StringUtils.isEmpty(selector)) {
			return Arrays.asList(new Selector());
		}
		ParserContext context = new ParserContext();
		for (char c : selector.toCharArray()) {
			context.getState().process(context, c);
		}
		context.getState().process(context, (char) 0);
		return context.getSelectors();
	}
}
