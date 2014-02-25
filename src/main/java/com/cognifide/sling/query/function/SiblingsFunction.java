package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.TreeProvider;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.ArrayIterator;

public class SiblingsFunction<T> implements ResourceToIteratorFunction<T> {

	private final TreeProvider<T> provider;

	public SiblingsFunction(TreeProvider<T> provider) {
		this.provider = provider;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> apply(T resource) {
		T parent = provider.getParent(resource);
		if (parent == null) {
			return new ArrayIterator<T>(resource);
		} else {
			return provider.listChildren(parent);
		}
	}
}
