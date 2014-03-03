package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;

public class ChildrenFunction<T> implements ElementToIteratorFunction<T> {

	private final TreeProvider<T> provider;

	public ChildrenFunction(TreeProvider<T> provider) {
		this.provider = provider;
	}

	@Override
	public Iterator<T> apply(T parent) {
		return provider.listChildren(parent);
	}
}