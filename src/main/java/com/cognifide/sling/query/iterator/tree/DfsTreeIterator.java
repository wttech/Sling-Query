package com.cognifide.sling.query.iterator.tree;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import com.cognifide.sling.query.TreeStructureProvider;
import com.cognifide.sling.query.iterator.AbstractIterator;

public class DfsTreeIterator<T> extends AbstractIterator<T> {

	private final Deque<Iterator<T>> queue = new LinkedList<Iterator<T>>();

	private final TreeStructureProvider<T> provider;

	public DfsTreeIterator(T root, TreeStructureProvider<T> provider) {
		this.provider = provider;
		queue.add(provider.getChildren(root));
	}

	@Override
	protected T getElement() {
		if (queue.isEmpty()) {
			return null;
		}
		if (queue.peekLast().hasNext()) {
			T next = queue.peekLast().next();
			queue.add(provider.getChildren(next));
			return next;
		} else {
			queue.pollLast();
			return getElement();
		}
	}
}
