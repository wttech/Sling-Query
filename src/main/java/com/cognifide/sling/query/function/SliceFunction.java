package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.SliceIterator;
import com.cognifide.sling.query.selector.Option;

public class SliceFunction<T> implements OptionIteratorToIteratorFunction<T> {

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
	public Iterator<Option<T>> apply(Iterator<Option<T>> resources) {
		if (to == null) {
			return new SliceIterator<T>(resources, from);
		} else {
			return new SliceIterator<T>(resources, from, to);
		}
	}
}