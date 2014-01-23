package com.cognifide.sling.query.iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;

public class ParentsIterator extends AbstractResourceIterator {

	private final ResourcePredicate until;

	private Resource currentResource;

	public ParentsIterator(ResourcePredicate until, Resource currentResource) {
		this.currentResource = currentResource;
		this.until = until;
	}

	@Override
	protected Resource getResource() {
		if (currentResource == null) {
			return null;
		}
		currentResource = currentResource.getParent();

		if (currentResource == null) {
			return null;
		}

		if (until != null && until.accepts(currentResource)) {
			return null;
		}

		return currentResource;
	}

}
