package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.SliceIterator;

public class SliceFunction<T> implements IteratorToIteratorFunction<T> {

	private final int from;

	private final Integer to;

	public SliceFunction(int from, int to) {
		this.from = from;
		this.to = to;
	}

	public SliceFunction(int from) {
		this.from = from;
		this.to = null;
	}

	@Override
	public Iterator<T> apply(Iterator<T> resources) {
		if (to == null) {
			return new SliceIterator<T>(resources, from);
		} else {
			return new SliceIterator<T>(resources, from, to);
		}
	}
}