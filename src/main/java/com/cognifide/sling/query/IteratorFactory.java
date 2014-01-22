package com.cognifide.sling.query;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;
import com.cognifide.sling.query.iterator.FunctionIterator;

public final class IteratorFactory {
	private IteratorFactory() {
	}

	public static Iterator<Resource> getIterator(Function<?, ?> function, Iterator<Resource> parentIterator) {
		if (function instanceof ResourceToResourceFunction) {
			ResourceToIteratorFunction wrappingFunction = new ResourceToIteratorWrapperFunction(
					(ResourceToResourceFunction) function);
			return new FunctionIterator(wrappingFunction, parentIterator);
		} else if (function instanceof ResourceToIteratorFunction) {
			return new FunctionIterator((ResourceToIteratorFunction) function, parentIterator);
		} else if (function instanceof IteratorToIteratorFunction) {
			return ((IteratorToIteratorFunction) function).apply(parentIterator);
		} else {
			throw new IllegalArgumentException("Don't know how to handle " + function.toString());
		}
	}
}
