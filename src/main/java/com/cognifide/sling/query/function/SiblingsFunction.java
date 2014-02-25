package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.TreeStructureProvider;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.ArrayIterator;

public class SiblingsFunction<T> implements ResourceToIteratorFunction<T> {

	private final TreeStructureProvider<T> provider;

	public SiblingsFunction(TreeStructureProvider<T> provider) {
		this.provider = provider;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> apply(T resource) {
		T parent = provider.getParent(resource);
		if (parent == null) {
			return new ArrayIterator<T>(resource);
		} else {
			return provider.getChildren(parent);
		}
	}
}
