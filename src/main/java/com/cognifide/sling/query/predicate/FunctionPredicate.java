package com.cognifide.sling.query.predicate;

import java.util.Iterator;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.iterator.ArrayIterator;

public class FunctionPredicate<T> implements Predicate<T> {

	private final Function<Iterator<T>, Iterator<T>> function;

	public FunctionPredicate(Function<Iterator<T>, Iterator<T>> function) {
		this.function = function;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean accepts(T value) {
		return function.apply(new ArrayIterator<T>(value)).hasNext();
	}

}
