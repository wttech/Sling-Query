package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.iterator.ExpandingIterator;

public class IteratorToIteratorFunctionWrapper<T> implements OptionIteratorToIteratorFunction<T> {

	private final Function<?, ?> function;

	public IteratorToIteratorFunctionWrapper(Function<?, ?> function) {
		this.function = function;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> parentIterator) {
		if (isEtoI(function)) {
			return getOptionIterator((ElementToIteratorFunction<T>) function, parentIterator);
		} else if (isOptionItoI(function)) {
			return getOptionIterator((OptionIteratorToIteratorFunction<T>) function, parentIterator);
		} else {
			throw new IllegalArgumentException("Don't know how to handle " + function.toString());
		}
	}

	private static <T> Iterator<Option<T>> getOptionIterator(ElementToIteratorFunction<T> function,
			Iterator<Option<T>> parentIterator) {
		return new ExpandingIterator<T>((ElementToIteratorFunction<T>) function, parentIterator);
	}

	private static <T> Iterator<Option<T>> getOptionIterator(OptionIteratorToIteratorFunction<T> function,
			Iterator<Option<T>> parentIterator) {
		return function.apply(parentIterator);
	}

	private static boolean isEtoI(Function<?, ?> function) {
		return function instanceof ElementToIteratorFunction;
	}

	private static boolean isOptionItoI(Function<?, ?> function) {
		return function instanceof OptionIteratorToIteratorFunction;
	}

}
