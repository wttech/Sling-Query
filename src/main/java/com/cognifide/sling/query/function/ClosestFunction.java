package com.cognifide.sling.query.function;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;

public class ClosestFunction implements ResourceToResourceFunction {

	private final ResourcePredicate predicate;

	public ClosestFunction(ResourcePredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public Resource apply(Resource resource) {
		Resource current = resource;
		do {
			current = current.getParent();
		} while (current != null && !predicate.accepts(current));
		return current;
	}
}