package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.IteratorUtils;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.iterator.EmptyElementFilter;
import com.cognifide.sling.query.selector.SelectorFunction;

public class HasFunction<T> implements ElementToIteratorFunction<T> {

	private final FindFunction<T> findFunction;

	private final SelectorFunction<T> selector;

	public HasFunction(String selectorString, SearchStrategy searchStrategy, TreeProvider<T> provider) {
		this.selector = new SelectorFunction<T>(selectorString, provider, searchStrategy);
		this.findFunction = new FindFunction<T>(searchStrategy, provider, selectorString);
	}

	@Override
	public Iterator<T> apply(T input) {
		Iterator<Option<T>> iterator = IteratorUtils.singleElementIterator(Option.of(input));
		iterator = new IteratorToIteratorFunctionWrapper<T>(findFunction).apply(iterator);
		iterator = selector.apply(iterator);
		if (new EmptyElementFilter<T>(iterator).hasNext()) {
			return IteratorUtils.singleElementIterator(input);
		} else {
			return IteratorUtils.emptyIterator();
		}
	}
}