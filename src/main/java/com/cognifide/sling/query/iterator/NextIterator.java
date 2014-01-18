package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;

public class NextIterator extends AbstractResourceIterator {

	private final ResourcePredicate until;

	private final Iterator<Resource> nextResources;

	private boolean finished;

	public NextIterator(ResourcePredicate until, Resource resource) {
		this.until = until;
		this.nextResources = rewindedIterator(resource);
		this.finished = false;
	}

	@Override
	protected Resource getResource() {
		if (finished) {
			return null;
		}
		while (nextResources.hasNext()) {
			Resource resource = nextResources.next();
			if (until != null && until.accepts(resource)) {
				finished = true;
				return null;
			}
			if (until == null) {
				finished = true;
			}
			return resource;
		}
		return null;
	}

	private Iterator<Resource> rewindedIterator(Resource resource) {
		Iterator<Resource> siblings = resource.getParent().listChildren();
		String resourceName = resource.getName();
		while (siblings.hasNext()) {
			Resource sibling = siblings.next();
			if (!sibling.getName().equals(resourceName)) {
				continue;
			}
		}
		return siblings;
	}

}
