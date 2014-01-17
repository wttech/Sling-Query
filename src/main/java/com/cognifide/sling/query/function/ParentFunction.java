package com.cognifide.sling.query.function;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.ResourceToResourceFunction;

public class ParentFunction implements ResourceToResourceFunction {

	@Override
	public Resource apply(Resource resource) {
		return resource.getParent();
	}

}
