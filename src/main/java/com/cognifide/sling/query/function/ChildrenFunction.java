package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.iterator.OptionalElementIterator;
import com.cognifide.sling.query.selector.OptionalElement;

public class ChildrenFunction<T> implements ElementToIteratorFunction<T> {

	private final TreeProvider<T> provider;

	public ChildrenFunction(TreeProvider<T> provider) {
		this.provider = provider;
	}

	@Override
	public Iterator<OptionalElement<T>> apply(T parent) {
		return new OptionalElementIterator<T>(provider.listChildren(parent), parent);
	}
}