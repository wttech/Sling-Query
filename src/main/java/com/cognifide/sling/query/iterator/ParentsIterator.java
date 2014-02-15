package com.cognifide.sling.query.iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Predicate;

public class ParentsIterator extends AbstractIterator<Resource> {

	private final Predicate<Resource> until;

	private Resource currentResource;

	public ParentsIterator(Predicate<Resource> until, Resource currentResource) {
		this.currentResource = currentResource;
		this.until = until;
	}

	@Override
	protected Resource getElement() {
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
