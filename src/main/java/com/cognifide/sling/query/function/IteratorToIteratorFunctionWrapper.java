package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.iterator.ExpandingIterator;

public class IteratorToIteratorFunctionWrapper<T> implements IteratorToIteratorFunction<T> {

	private final Function<?, ?> function;

	public IteratorToIteratorFunctionWrapper(Function<?, ?> function) {
		this.function = function;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> parentIterator) {
		if (function instanceof ElementToIteratorFunction) {
			return getOptionIterator((ElementToIteratorFunction<T>) function, parentIterator);
		} else if (function instanceof IteratorToIteratorFunction) {
			return ((IteratorToIteratorFunction<T>) function).apply(parentIterator);
		} else {
			throw new IllegalArgumentException("Don't know how to handle " + function.toString());
		}
	}

	private static <T> Iterator<Option<T>> getOptionIterator(ElementToIteratorFunction<T> function,
			Iterator<Option<T>> parentIterator) {
		return new ExpandingIterator<T>((ElementToIteratorFunction<T>) function, parentIterator);
	}
}
