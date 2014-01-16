package com.cognifide.sling.query;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

public interface Operation {
	Iterator<Resource> getResources(Resource resource);
	
}
