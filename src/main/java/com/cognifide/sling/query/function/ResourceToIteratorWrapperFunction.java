package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.api.function.ElementToElementFunction;
import com.cognifide.sling.query.iterator.ArrayIterator;

public class ResourceToIteratorWrapperFunction<T> implements ElementToIteratorFunction<T> {

	private final ElementToElementFunction<T> wrappedFunction;

	public ResourceToIteratorWrapperFunction(ElementToElementFunction<T> wrappedFunction) {
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
