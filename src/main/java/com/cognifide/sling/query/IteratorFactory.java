package com.cognifide.sling.query;

import java.util.Iterator;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;
import com.cognifide.sling.query.iterator.FunctionIterator;

public final class IteratorFactory {
	private IteratorFactory() {
	}

	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> getIterator(Function<?, ?> function, Iterator<T> parentIterator) {
		if (function instanceof ResourceToResourceFunction) {
			ResourceToIteratorFunction<T> wrappingFunction = new ResourceToIteratorWrapperFunction<T>(
					(ResourceToResourceFunction<T>) function);
			return new FunctionIterator<T>(wrappingFunction, parentIterator);
		} else if (function instanceof ResourceToIteratorFunction) {
			return new FunctionIterator<T>((ResourceToIteratorFunction<T>) function, parentIterator);
		} else if (function instanceof IteratorToIteratorFunction) {
			return ((IteratorToIteratorFunction<T>) function).apply(parentIterator);
		} else {
			throw new IllegalArgumentException("Don't know how to handle " + function.toString());
		}
	}
}
