package com.cognifide.sling.query.iterator.tree;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import com.cognifide.sling.query.TreeStructureProvider;
import com.cognifide.sling.query.iterator.AbstractIterator;

public class BfsTreeIterator<T> extends AbstractIterator<T> {

	private final Deque<T> queue = new LinkedList<T>();

	private final TreeStructureProvider<T> provider;

	private Iterator<T> currentIterator;

	public BfsTreeIterator(T root, TreeStructureProvider<T> provider) {
		this.currentIterator = provider.getChildren(root);
		this.provider = provider;
	}

	@Override
	protected T getElement() {
		if (currentIterator.hasNext()) {
			T resource = currentIterator.next();
			queue.add(resource);
			return resource;
		}

		if (!queue.isEmpty()) {
			currentIterator = provider.getChildren(queue.pop());
			return getElement();
		}

		return null;
	}
}
