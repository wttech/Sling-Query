package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.ArrayIterator;
import com.cognifide.sling.query.iterator.EmptyIterator;
import com.cognifide.sling.query.selector.Selector;

public class ClosestFunction implements ResourceToIteratorFunction {

	private final Selector selector;

	public ClosestFunction(Selector selector) {
		this.selector = selector;
	}

	@Override
	public Iterator<Resource> apply(Resource resource) {
		Resource current = resource;
		while (current != null) {
			Iterator<Resource> iterator = new ArrayIterator(current);
			iterator = selector.apply(iterator);
			if (iterator.hasNext()) {
				return iterator;
			}
			current = current.getParent();
		}
		return EmptyIterator.INSTANCE;
	}
}