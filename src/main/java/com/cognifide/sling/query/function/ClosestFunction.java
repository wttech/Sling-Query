package com.cognifide.sling.query.function;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.ElementToElementFunction;
import com.cognifide.sling.query.selector.OptionalElement;

public class ClosestFunction<T> implements ElementToElementFunction<T> {

	private final Predicate<T> predicate;

	private final TreeProvider<T> provider;

	public ClosestFunction(Predicate<T> predicate, TreeProvider<T> provider) {
		this.predicate = predicate;
		this.provider = provider;
	}

	@Override
	public OptionalElement<T> apply(T resource) {
		T current = resource;
		while (current != null) {
			if (predicate.accepts(current)) {
				return new OptionalElement<T>(current, resource);
			}
			current = provider.getParent(current);
		}
		return new OptionalElement<T>(null, resource);
	}
}