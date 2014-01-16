package com.cognifide.sling.query;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.sling.api.resource.Resource;

public class SlingIterator implements Iterator<Resource> {

	private final List<Operation> operations;

	private final Deque<Iterator<Resource>> iterators;

	private Resource nextResource;

	public SlingIterator(List<Operation> operations, List<Resource> resources) {
		this.operations = new ArrayList<Operation>(operations);
		this.iterators = new LinkedList<Iterator<Resource>>();
		this.iterators.add(resources.iterator());
	}

	@Override
	public boolean hasNext() {
		if (nextResource == null) {
			nextResource = computeNextResource();
		}
		return nextResource != null;
	}

	@Override
	public Resource next() {
		Resource resource = null;
		if (nextResource == null) {
			resource = computeNextResource();
		} else {
			resource = nextResource;
			nextResource = null;
		}
		if (resource != null) {
			return resource;
		} else {
			throw new NoSuchElementException();
		}
	}

	private Resource computeNextResource() {
		if (iterators.isEmpty()) {
			return null;
		}
		if (!iterators.peekLast().hasNext()) {
			iterators.pollLast();
			return computeNextResource();
		}
		Resource resource = iterators.peekLast().next();
		int addedOperations = iterators.size() - 1;
		if (addedOperations < operations.size()) {
			Iterator<Resource> newIterator = operations.get(addedOperations).getResources(resource);
			iterators.addLast(newIterator);
			return computeNextResource();
		} else {
			return resource;
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
