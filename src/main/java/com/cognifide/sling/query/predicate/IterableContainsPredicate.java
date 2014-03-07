package com.cognifide.sling.query.predicate;

import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.TreeProvider;

public class IterableContainsPredicate<T> implements Predicate<T> {

	private final Iterable<T> iterable;

	private final TreeProvider<T> provider;

	public IterableContainsPredicate(Iterable<T> iterable, TreeProvider<T> provider) {
		this.iterable = new LazyList<T>(iterable.iterator());
		this.provider = provider;
	}

	@Override
	public boolean accepts(T element) {
		for (T t : iterable) {
			if (provider.sameElement(t, element)) {
				return true;
			}
		}
		return false;
	}
}
