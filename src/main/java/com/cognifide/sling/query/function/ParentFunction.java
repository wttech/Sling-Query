package com.cognifide.sling.query.function;

import com.cognifide.sling.query.TreeStructureProvider;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;

public class ParentFunction<T> implements ResourceToResourceFunction<T> {

	private final TreeStructureProvider<T> provider;

	public ParentFunction(TreeStructureProvider<T> provider) {
		this.provider = provider;
	}

	@Override
	public T apply(T resource) {
		return provider.getParent(resource);
	}

}
