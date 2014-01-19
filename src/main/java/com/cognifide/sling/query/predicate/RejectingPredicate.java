package com.cognifide.sling.query.predicate;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;

public class RejectingPredicate implements ResourcePredicate {

	private final ResourcePredicate predicate;

	public RejectingPredicate() {
		this(new AcceptingPredicate());
	}

	public RejectingPredicate(ResourcePredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean accepts(Resource resource) {
		return !predicate.accepts(resource);
	}

}
