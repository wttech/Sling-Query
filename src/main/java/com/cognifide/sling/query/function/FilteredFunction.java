package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.selector.SelectorFunction;

public class FilteredFunction<T> implements IteratorToIteratorFunction<T> {
	private final Function<?, ?> function;

	private final SelectorFunction<T> selector;

	public FilteredFunction(Function<?, ?> function, String selector, SearchStrategy strategy,
			TreeProvider<T> provider) {
		this.function = function;
		if (StringUtils.isBlank(selector)) {
			this.selector = null;
		} else {
			this.selector = new SelectorFunction<T>(selector, provider, strategy);
		}
	}

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		Iterator<Option<T>> result = new IteratorToIteratorFunctionWrapper<T>(function).apply(input);
		if (selector != null) {
			result = selector.apply(result);
		}
		return result;

	}
}
