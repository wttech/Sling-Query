package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.sling.api.resource.Resource;

public abstract class AbstractResourceIterator implements Iterator<Resource> {

	private Resource currentResource;

	@Override
	public boolean hasNext() {
		currentResource = getResource();
		return currentResource != null;
	}

	@Override
	public Resource next() {
		Resource result;
		if (currentResource != null) {
			result = currentResource;
			currentResource = null;
		} else {
			result = getResource();
		}
		if (result == null) {
			throw new NoSuchElementException();
		} else {
			return result;
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	protected abstract Resource getResource();
}
