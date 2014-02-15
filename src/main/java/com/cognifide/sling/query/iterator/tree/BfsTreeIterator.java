package com.cognifide.sling.query.iterator.tree;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.iterator.AbstractIterator;

public class BfsTreeIterator extends AbstractIterator<Resource> {

	private final Deque<Resource> queue = new LinkedList<Resource>();

	private Iterator<Resource> currentIterator;

	public BfsTreeIterator(Resource root) {
		this.currentIterator = root.listChildren();
	}

	@Override
	protected Resource getElement() {
		if (currentIterator.hasNext()) {
			Resource resource = currentIterator.next();
			queue.add(resource);
			return resource;
		}

		if (!queue.isEmpty()) {
			currentIterator = queue.pop().listChildren();
			return getElement();
		}

		return null;
	}
}
