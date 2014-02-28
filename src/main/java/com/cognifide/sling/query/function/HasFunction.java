package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.ElementToElementFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;
import com.cognifide.sling.query.selector.SelectorFunction;

public class HasFunction<T> implements ElementToElementFunction<T> {

	private final FindFunction<T> findFunction;

	private final Predicate<T> predicate;

	public HasFunction(String selectorString, SearchStrategy searchStrategy, TreeProvider<T> provider) {
		this.findFunction = new FindFunction<T>(selectorString, searchStrategy, provider);
		this.predicate = SelectorFunction.parse(selectorString, searchStrategy, provider);
	}

	@Override
	public T apply(T input) {
		Iterator<T> iterator = findFunction.apply(input);
		iterator = new FilteringIteratorWrapper<T>(iterator, predicate);
		if (iterator.hasNext()) {
			return input;
		} else {
			return null;
		}
	}
}