package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.EmptyElementFilter;
import com.cognifide.sling.query.iterator.IteratorFactory;
import com.cognifide.sling.query.iterator.OptionalElementIterator;
import com.cognifide.sling.query.selector.Option;
import com.cognifide.sling.query.selector.SelectorFunction;

public class FunctionWithSelector<T> implements OptionIteratorToIteratorFunction<T> {
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
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		Iterator<T> newIterator = IteratorFactory.getIterator(function, new EmptyElementFilter<T>(input));
		if (selector != null) {
			newIterator = new EmptyElementFilter<T>(
					selector.apply(new OptionalElementIterator<T>(newIterator)));
		}
		return new OptionalElementIterator<T>(newIterator);
	}
}
