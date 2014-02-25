package com.cognifide.sling.query;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.selector.SelectorFunction;

public class FunctionWithSelector<T> implements IteratorToIteratorFunction<T> {
	private final Function<?, ?> function;

	private final SelectorFunction<T> selector;

	public FunctionWithSelector(Function<?, ?> function, String selector, SearchStrategy strategy,
			TreeProvider<T> provider) {
		this.function = function;
		if (StringUtils.isBlank(selector)) {
			this.selector = null;
		} else {
			this.selector = SelectorFunction.parse(selector, strategy, provider);
		}
	}

	@Override
	public Iterator<T> apply(Iterator<T> input) {
		Iterator<T> newIterator = IteratorFactory.getIterator(function, input);
		if (selector != null) {
			newIterator = selector.apply(newIterator);
		}
		return newIterator;
	}
}
