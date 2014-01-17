package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.NextIterator;

public class NextFunction implements ResourceToIteratorFunction {

	private final ResourcePredicate predicate;

	private final ResourcePredicate until;

	public NextFunction(ResourcePredicate predicate) {
		this.predicate = predicate;
		this.until = null;
	}

	public NextFunction(ResourcePredicate predicate, ResourcePredicate until) {
		this.predicate = predicate;
		this.until = until;
	}

	@Override
	public Iterator<Resource> apply(Resource resource) {
		return new NextIterator(predicate, until, resource);
	}

}
