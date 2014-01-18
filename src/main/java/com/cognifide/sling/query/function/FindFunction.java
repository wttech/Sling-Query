package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.TreeIterator;

public class FindFunction implements ResourceToIteratorFunction {

	@Override
	public Iterator<Resource> apply(Resource resource) {
		return new TreeIterator(resource);
	}
}