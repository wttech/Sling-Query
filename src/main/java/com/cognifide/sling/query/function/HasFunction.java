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

	private final IteratorToIteratorFunction<T> findFunction;

	private final IteratorToIteratorFunction<T> filter;

	private HasFunction(FindFunction<T> findFunction, IteratorToIteratorFunction<T> filter) {
		this.findFunction = new IteratorToIteratorFunctionWrapper<T>(findFunction);
		this.filter = filter;
	}

	public HasFunction(String selectorString, SearchStrategy searchStrategy, TreeProvider<T> provider) {
		this(new FindFunction<T>(searchStrategy, provider, selectorString), new SelectorFunction<T>(
				selectorString, provider, searchStrategy));
	}

	public HasFunction(Predicate<T> predicate, SearchStrategy searchStrategy, TreeProvider<T> provider) {
		this(new FindFunction<T>(searchStrategy, provider, ""), new FilterFunction<T>(predicate));
	}

	public HasFunction(Iterable<T> iterable, TreeProvider<T> provider) {
		this.findFunction = new DescendantFunction<T>(iterable, provider);
		this.filter = new IdentityFunction<T>();
	}

	@Override
	public Iterator<T> apply(T input) {
		Iterator<Option<T>> iterator = IteratorUtils.singleElementIterator(Option.of(input, 0));
		iterator = findFunction.apply(iterator);
		iterator = filter.apply(iterator);
		if (new EmptyElementFilter<T>(iterator).hasNext()) {
			return IteratorUtils.singleElementIterator(input);
		} else {
			return IteratorUtils.emptyIterator();
		}
	}
}