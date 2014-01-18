package com.cognifide.sling.query.function;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.ResourceToResourceFunction;

public class IdentityFunction implements ResourceToResourceFunction {

	@Override
	public Resource apply(Resource resource) {
		return resource;
	}

}
