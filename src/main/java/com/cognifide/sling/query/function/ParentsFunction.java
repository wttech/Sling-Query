package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.TreeStructureProvider;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.ParentsIterator;

public class ParentsFunction<T> implements ResourceToIteratorFunction<T> {

	private final Predicate<T> until;

	private final TreeStructureProvider<T> provider;

	public ParentsFunction(Predicate<T> until, TreeStructureProvider<T> provider) {
		this.until = until;
		this.provider = provider;
	}

	@Override
	public Iterator<T> apply(T resource) {
		return new ParentsIterator<T>(until, resource, provider);
	}

}
