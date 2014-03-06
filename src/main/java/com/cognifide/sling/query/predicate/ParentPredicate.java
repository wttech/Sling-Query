package com.cognifide.sling.query.predicate;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.TreeProvider;

public class ParentPredicate<T> implements Predicate<T> {

	private final TreeProvider<T> provider;

	public ParentPredicate(TreeProvider<T> provider) {
		this.provider = provider;
	}

	@Override
	public boolean accepts(T resource) {
		return provider.listChildren(resource).hasNext();
	}

}
