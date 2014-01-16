package com.cognifide.sling.query;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.sling.api.resource.Resource;

public class OperationIterator implements Iterator<Resource> {

	private final Operation operation;

	private final Iterator<Resource> parentIterator;

	private Iterator<Resource> operationIterator;

	private Resource currentResource;

	public OperationIterator(Operation operation, Iterator<Resource> parentIterator) {
		this.operation = operation;
		this.parentIterator = parentIterator;
	}

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

	private Resource getResource() {
		if (operationIterator != null && operationIterator.hasNext()) {
			return operationIterator.next();
		}
		while (parentIterator.hasNext()) {
			Resource parentResource = parentIterator.next();
			operationIterator = operation.getResources(parentResource);
			if (operationIterator.hasNext()) {
				return operationIterator.next();
			}
		}
		return null;
	}

}
