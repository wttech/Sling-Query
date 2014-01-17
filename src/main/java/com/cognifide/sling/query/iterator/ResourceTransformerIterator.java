package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.ResourceToResourceFunction;

public class ResourceTransformerIterator extends AbstractResourceIterator {

	private final ResourceToResourceFunction function;

	private final Iterator<Resource> parentIterator;

	public ResourceTransformerIterator(ResourceToResourceFunction function, Iterator<Resource> parentIterator) {
		this.function = function;
		this.parentIterator = parentIterator;
	}

	@Override
	protected Resource getResource() {
		while (parentIterator.hasNext()) {
			Resource resource = function.apply(parentIterator.next());
			if (resource != null) {
				return resource;
			}
		}
		return null;
	}
}
