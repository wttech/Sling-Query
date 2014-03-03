package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.IteratorUtils;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;

public class ParentFunction<T> implements ElementToIteratorFunction<T> {

	private final TreeProvider<T> provider;

	public ParentFunction(TreeProvider<T> provider) {
		this.provider = provider;
	}

	@Override
	public Iterator<T> apply(T element) {
		T parent = provider.getParent(element);
		if (parent == null) {
			return IteratorUtils.emptyIterator();
		} else {
			return IteratorUtils.singleElementIterator(parent);
		}
	}

}
