package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.ArrayIterator;

public class LastFunction implements IteratorToIteratorFunction {

	@Override
	public Iterator<Resource> apply(Iterator<Resource> input) {
		Resource lastElement = null;
		while (input.hasNext()) {
			lastElement = input.next();
		}
		return new ArrayIterator(lastElement);
	}

}
