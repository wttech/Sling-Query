package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.iterator.UniqueIterator;

public class UniqueFunction<T> implements IteratorToIteratorFunction<T> {

	private final TreeProvider<T> treeProvider;

	public UniqueFunction(TreeProvider<T> treeProvider) {
		this.treeProvider = treeProvider;
	}

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		return new UniqueIterator<T>(input, treeProvider);
	}

}
