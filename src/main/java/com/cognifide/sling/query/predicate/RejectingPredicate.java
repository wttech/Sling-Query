package com.cognifide.sling.query.predicate;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;

public class RejectingPredicate implements ResourcePredicate {

	@Override
	public boolean accepts(Resource resource) {
		return false;
	}

}
