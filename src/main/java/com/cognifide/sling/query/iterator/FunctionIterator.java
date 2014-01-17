package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;

public class FunctionIterator extends AbstractResourceIterator {

	private final ResourceToIteratorFunction function;

	private final Iterator<Resource> parentIterator;

	private Iterator<Resource> currentIterator;

	public FunctionIterator(ResourceToIteratorFunction function, Iterator<Resource> parentIterator) {
		this.function = function;
		this.parentIterator = parentIterator;
	}

	@Override
	protected Resource getResource() {
		if (currentIterator != null && currentIterator.hasNext()) {
			return currentIterator.next();
		}
		while (parentIterator.hasNext()) {
			Resource parentResource = parentIterator.next();
			currentIterator = function.apply(parentResource);
			if (currentIterator.hasNext()) {
				return currentIterator.next();
			}
		}
		return null;
	}
}
