package com.cognifide.sling.query;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.selector.Selector;

public class FunctionWithSelector implements IteratorToIteratorFunction {
	private final Function<?, ?> function;

	private final Selector selector;

	public FunctionWithSelector(Function<?, ?> function, String selector) {
		this.function = function;
		if (StringUtils.isBlank(selector)) {
			this.selector = null;
		} else {
			this.selector = new Selector(selector);
		}
	}

	@Override
	public Iterator<Resource> apply(Iterator<Resource> input) {
		Iterator<Resource> newIterator = IteratorFactory.getIterator(function, input);
		if (selector != null) {
			newIterator = selector.apply(newIterator);
		}
		return newIterator;
	}
}
