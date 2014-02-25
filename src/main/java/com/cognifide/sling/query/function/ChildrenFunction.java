package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.TreeProvider;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;

public class ChildrenFunction<T> implements ResourceToIteratorFunction<T> {

	private final TreeProvider<T> provider;

	public ChildrenFunction(TreeProvider<T> provider) {
		this.provider = provider;
	}

	@Override
	public Iterator<T> apply(T parent) {
		return provider.listChildren(parent);
	}
}