package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.IteratorUtils;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;

public class ClosestFunction<T> implements ElementToIteratorFunction<T> {

	private final Predicate<T> predicate;

	private final TreeProvider<T> provider;

	public ClosestFunction(Predicate<T> predicate, TreeProvider<T> provider) {
		this.predicate = predicate;
		this.provider = provider;
	}

	@Override
	public Iterator<T> apply(T resource) {
		T current = resource;
		while (current != null) {
			if (predicate.accepts(current)) {
				return IteratorUtils.singleElementIterator(current);
			}
			current = provider.getParent(current);
		}
		return IteratorUtils.emptyIterator();
	}
}