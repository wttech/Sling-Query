package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.TreeStructureProvider;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.ArrayIterator;
import com.cognifide.sling.query.selector.Selector;

public class ClosestFunction<T> implements ResourceToIteratorFunction<T> {

	private final Selector<T> selector;

	private final TreeStructureProvider<T> provider;

	public ClosestFunction(Selector<T> selector, TreeStructureProvider<T> provider) {
		this.selector = selector;
		this.provider = provider;
	}

	@Override
	public Iterator<T> apply(T resource) {
		T current = resource;
		while (current != null) {
			@SuppressWarnings("unchecked")
			Iterator<T> iterator = new ArrayIterator<T>(current);
			iterator = selector.apply(iterator);
			if (iterator.hasNext()) {
				return iterator;
			}
			current = provider.getParent(current);
		}
		return ArrayIterator.getEmptyIterator();
	}
}