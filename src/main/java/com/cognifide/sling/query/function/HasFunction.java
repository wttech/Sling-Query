package com.cognifide.sling.query.function;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;
import com.cognifide.sling.query.iterator.TreeIterator;

public class HasFunction implements ResourceToResourceFunction {

	private final ResourcePredicate predicate;

	public HasFunction(ResourcePredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public Resource apply(Resource input) {
		TreeIterator iterator = new TreeIterator(input);
		while (iterator.hasNext()) {
			if (predicate.accepts(iterator.next())) {
				return input;
			}
		}
		return null;
	}

}
