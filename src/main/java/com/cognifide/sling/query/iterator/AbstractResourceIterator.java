package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.sling.api.resource.Resource;

public abstract class AbstractResourceIterator implements Iterator<Resource> {

	private Resource currentResource;

	@Override
	public boolean hasNext() {
		if (currentResource == null) {
			currentResource = getResource();
		}
		return currentResource != null;
	}

	@Override
	public Resource next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		Resource result = currentResource;
		currentResource = null;
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	protected abstract Resource getResource();
}
