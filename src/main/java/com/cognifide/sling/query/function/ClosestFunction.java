package com.cognifide.sling.query.function;

import com.cognifide.sling.query.TreeProvider;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;

public class ClosestFunction<T> implements ResourceToResourceFunction<T> {

	private final Predicate<T> predicate;

	private final TreeProvider<T> provider;

	public ClosestFunction(Predicate<T> predicate, TreeProvider<T> provider) {
		this.predicate = predicate;
		this.provider = provider;
	}

	@Override
	public T apply(T resource) {
		T current = resource;
		while (current != null) {
			if (predicate.accepts(current)) {
				return current;
			}
			current = provider.getParent(current);
		}
		return null;
	}
}