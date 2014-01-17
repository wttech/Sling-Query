package com.cognifide.sling.query.api.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;

public interface ResourceToIteratorFunction extends Function<Resource, Iterator<Resource>> {

	Iterator<Resource> apply(Resource resource);

}
