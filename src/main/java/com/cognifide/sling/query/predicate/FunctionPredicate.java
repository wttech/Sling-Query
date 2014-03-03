package com.cognifide.sling.query.predicate;

import java.util.Iterator;

import com.cognifide.sling.query.IteratorUtils;
import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.Predicate;

public class FunctionPredicate<T> implements Predicate<T> {

	private final Function<Iterator<T>, Iterator<T>> function;

	public FunctionPredicate(Function<Iterator<T>, Iterator<T>> function) {
		this.function = function;
	}

	@Override
	public boolean accepts(T value) {
		return function.apply(IteratorUtils.singleElementIterator(value)).hasNext();
	}

}
