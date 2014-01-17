package com.cognifide.sling.query.api.function;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;

public interface ResourceToResourceFunction extends Function<Resource, Resource> {

	Resource apply(Resource resource);

}