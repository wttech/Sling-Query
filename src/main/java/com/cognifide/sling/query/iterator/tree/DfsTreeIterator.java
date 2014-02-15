package com.cognifide.sling.query.iterator.tree;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.iterator.AbstractIterator;

public class DfsTreeIterator extends AbstractIterator<Resource> {

	private final Deque<Iterator<Resource>> queue = new LinkedList<Iterator<Resource>>();

	public DfsTreeIterator(Resource root) {
		queue.add(root.listChildren());
	}

	@Override
	protected Resource getElement() {
		if (queue.isEmpty()) {
			return null;
		}
		if (queue.peekLast().hasNext()) {
			Resource next = queue.peekLast().next();
			queue.add(next.listChildren());
			return next;
		} else {
			queue.pollLast();
			return getElement();
		}
	}
}
