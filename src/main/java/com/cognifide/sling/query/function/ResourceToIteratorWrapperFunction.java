package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.IteratorUtils;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.api.function.ElementToElementFunction;

public class ResourceToIteratorWrapperFunction<T> implements ElementToIteratorFunction<T> {

	private final ElementToElementFunction<T> wrappedFunction;

	public ResourceToIteratorWrapperFunction(ElementToElementFunction<T> wrappedFunction) {
		this.wrappedFunction = wrappedFunction;
	}

	@Override
	public Iterator<T> apply(T input) {
		T result = wrappedFunction.apply(input);
		if (result == null) {
			return IteratorUtils.getEmptyIterator();
		} else {
			return IteratorUtils.singleElementIterator(result);
		}
	}

}
