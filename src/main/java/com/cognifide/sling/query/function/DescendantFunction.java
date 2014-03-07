package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.iterator.DescendantsIterator;

public class DescendantFunction<T> implements IteratorToIteratorFunction<T> {

	private final Iterable<T> descendants;

	private final TreeProvider<T> provider;

	public DescendantFunction(Iterable<T> descendants, TreeProvider<T> provider) {
		this.descendants = descendants;
		this.provider = provider;
	}

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		return new DescendantsIterator<T>(input, descendants.iterator(), provider);
	}

}
