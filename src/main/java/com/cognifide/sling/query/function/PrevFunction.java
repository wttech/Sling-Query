package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.iterator.SiblingsIterator;
import com.cognifide.sling.query.iterator.SiblingsIterator.Type;

public class PrevFunction<T> implements ElementToIteratorFunction<T> {

	private final Predicate<T> until;
	
	private final TreeProvider<T> provider;

	public PrevFunction(Predicate<T> until, TreeProvider<T> provider) {
		this.until = until;
		this.provider = provider;
	}

	@Override
	public Iterator<T> apply(T resource) {
		return new SiblingsIterator<T>(until, resource, Type.PREV, provider);
	}
}