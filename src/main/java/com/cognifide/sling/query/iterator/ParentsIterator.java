package com.cognifide.sling.query.iterator;

import org.apache.sling.api.resource.Resource;

public class ParentsIterator extends AbstractResourceIterator {

	private Resource currentResource;

	public ParentsIterator(Resource resource) {
		this.currentResource = resource;
	}

	@Override
	protected Resource getResource() {
		if (currentResource == null) {
			return null;
		}
		currentResource = currentResource.getParent();
		return currentResource;
	}

}
