package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.api.function.ElementToElementFunction;
import com.cognifide.sling.query.function.ResourceToIteratorWrapperFunction;

public final class IteratorFactory {
	private IteratorFactory() {
	}

	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> getIterator(Function<?, ?> function, Iterator<T> parentIterator) {
		if (function instanceof ElementToElementFunction) {
			ElementToIteratorFunction<T> wrappingFunction = new ResourceToIteratorWrapperFunction<T>(
					(ElementToElementFunction<T>) function);
			return new FunctionIterator<T>(wrappingFunction, parentIterator);
		} else if (function instanceof ElementToIteratorFunction) {
			return new FunctionIterator<T>((ElementToIteratorFunction<T>) function, parentIterator);
		} else if (function instanceof IteratorToIteratorFunction) {
			return ((IteratorToIteratorFunction<T>) function).apply(parentIterator);
		} else {
			throw new IllegalArgumentException("Don't know how to handle " + function.toString());
		}
	}
}
