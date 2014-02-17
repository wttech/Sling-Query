package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.ExcludingIterator;
import com.cognifide.sling.query.selector.Selector;

public class NotFunction implements IteratorToIteratorFunction {

	private final Selector selector;

	public NotFunction(Selector selector) {
		this.selector = selector;
	}

	@Override
	public Iterator<Resource> apply(Iterator<Resource> input) {
		return new ExcludingIterator<Resource>(input, selector);
	}
}
