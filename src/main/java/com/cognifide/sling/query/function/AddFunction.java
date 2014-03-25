package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.iterator.MergingIterator;
import com.cognifide.sling.query.iterator.OptionDecoratingIterator;

public class AddFunction<T> implements IteratorToIteratorFunction<T> {

	private final Iterable<T> iterable;

	public AddFunction(Iterable<T> iterable) {
		this.iterable = iterable;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		return new MergingIterator<Option<T>>(input, new OptionDecoratingIterator<T>(iterable.iterator()));
	}

}
