package com.cognifide.sling.query.iterator;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.ResourcePredicate;

public class TreeIterator extends AbstractResourceIterator {

	private final ResourcePredicate predicate;

	private final Deque<Resource> queue = new LinkedList<Resource>();

	private Iterator<Resource> currentIterator;

	public TreeIterator(Resource root, ResourcePredicate predicate) {
		this.predicate = predicate;
		this.currentIterator = root.listChildren();
	}

	@Override
	protected Resource getResource() {
		while (currentIterator.hasNext()) {
			Resource resource = currentIterator.next();
			queue.add(resource);
			if (predicate.accepts(resource)) {
				return resource;
			}
		}

		if (!queue.isEmpty()) {
			currentIterator = queue.pop().listChildren();
			return getResource();
		}

		return null;
	}
}
