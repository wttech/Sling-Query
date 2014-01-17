package com.cognifide.sling.query.iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;

public class ParentsIterator extends AbstractResourceIterator {

	private final ResourcePredicate predicate;

	private Resource currentResource;

	public ParentsIterator(ResourcePredicate predicate, Resource resource) {
		this.predicate = predicate;
		this.currentResource = resource;
	}

	@Override
	protected Resource getResource() {
		do {
			currentResource = currentResource.getParent();
		} while (currentResource != null && !predicate.accepts(currentResource));
		return currentResource;
	}

}
