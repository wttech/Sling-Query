package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.TreeStructureProvider;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;
import com.cognifide.sling.query.selector.Selector;

public class HasFunction<T> implements ResourceToResourceFunction<T> {

	private final FindFunction<T> findFunction;

	private final Predicate<T> predicate;
	
	public HasFunction(String selectorString, SearchStrategy searchStrategy, TreeStructureProvider<T> provider) {
		this.findFunction = new FindFunction<T>(selectorString, searchStrategy, provider);
		this.predicate = new Selector<T>(selectorString, searchStrategy, provider).asPredicate();
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