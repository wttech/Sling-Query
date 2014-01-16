package com.cognifide.sling.query;

import org.apache.sling.api.resource.Resource;

public interface ResourcePredicate {
	boolean accepts(Resource resource);
}
