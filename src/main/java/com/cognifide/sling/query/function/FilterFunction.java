package com.cognifide.sling.query.function;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;

public class FilterFunction implements ResourceToResourceFunction {

	private final ResourcePredicate predicate;

	public FilterFunction(ResourcePredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public Resource apply(Resource resource) {
		return predicate.accepts(resource) ? resource : null;
	}

}
