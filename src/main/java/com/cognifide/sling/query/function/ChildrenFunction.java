package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.TreeStructureProvider;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;

public class ChildrenFunction<T> implements ResourceToIteratorFunction<T> {

	private final TreeStructureProvider<T> provider;

	public ChildrenFunction(TreeStructureProvider<T> provider) {
		this.provider = provider;
	}

	@Override
	public Iterator<T> apply(T parent) {
		return provider.getChildren(parent);
	}
}