package com.cognifide.sling.query.api;

import org.apache.sling.api.resource.Resource;

public interface ResourcePredicate {
	boolean accepts(Resource resource);
}
