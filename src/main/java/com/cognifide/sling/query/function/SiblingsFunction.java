package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.IteratorUtils;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;

public class SiblingsFunction<T> implements ElementToIteratorFunction<T> {

	private final TreeProvider<T> provider;

	public SiblingsFunction(TreeProvider<T> provider) {
		this.provider = provider;
	}

	@Override
	public Iterator<T> apply(T resource) {
		T parent = provider.getParent(resource);
		if (parent == null) {
			return IteratorUtils.singleElementIterator(resource);
		} else {
			return provider.listChildren(parent);
		}
	}
}
