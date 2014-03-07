package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.IteratorUtils;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.iterator.EmptyElementFilter;
import com.cognifide.sling.query.selector.SelectorFunction;

public class HasFunction<T> implements ElementToIteratorFunction<T> {

	private final FindFunction<T> findFunction;

	private final IteratorToIteratorFunction<T> filter;

	public HasFunction(String selectorString, SearchStrategy searchStrategy, TreeProvider<T> provider) {
		this.findFunction = new FindFunction<T>(searchStrategy, provider, selectorString);
		this.filter = new SelectorFunction<T>(selectorString, provider, searchStrategy);
	}

	public HasFunction(Predicate<T> predicate, SearchStrategy searchStrategy, TreeProvider<T> provider) {
		this.findFunction = new FindFunction<T>(searchStrategy, provider, "");
		this.filter = new FilterFunction<T>(predicate);
	}

	@Override
	public Iterator<T> apply(T input) {
		Iterator<Option<T>> iterator = IteratorUtils.singleElementIterator(Option.of(input, 0));
		iterator = new IteratorToIteratorFunctionWrapper<T>(findFunction).apply(iterator);
		iterator = filter.apply(iterator);
		if (new EmptyElementFilter<T>(iterator).hasNext()) {
			return IteratorUtils.singleElementIterator(input);
		} else {
			return IteratorUtils.emptyIterator();
		}
	}
}