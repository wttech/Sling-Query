package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.NextIterator;

public class NextFunction implements ResourceToIteratorFunction {

	private final Predicate<Resource> until;

	public NextFunction(Predicate<Resource> until) {
		this.until = until;
	}

	@Override
	public Iterator<Resource> apply(Resource resource) {
		return new NextIterator(until, resource);
	}
}