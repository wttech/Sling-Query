package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;

public class ChildrenFunction implements ResourceToIteratorFunction {

	@Override
	public Iterator<Resource> apply(Resource resource) {
		return resource.listChildren();
	}
}