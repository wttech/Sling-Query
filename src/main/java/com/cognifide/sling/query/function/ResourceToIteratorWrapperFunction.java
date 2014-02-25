package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;
import com.cognifide.sling.query.iterator.ArrayIterator;

public class ResourceToIteratorWrapperFunction<T> implements ResourceToIteratorFunction<T> {

	private final ResourceToResourceFunction<T> wrappedFunction;

	public ResourceToIteratorWrapperFunction(ResourceToResourceFunction<T> wrappedFunction) {
		this.wrappedFunction = wrappedFunction;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> apply(T input) {
		T result = wrappedFunction.apply(input);
		if (result == null) {
			return ArrayIterator.getEmptyIterator();
		} else {
			return new ArrayIterator<T>(result);
		}
	}

}
