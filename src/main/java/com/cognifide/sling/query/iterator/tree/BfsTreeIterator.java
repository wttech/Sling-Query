package com.cognifide.sling.query.iterator.tree;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.iterator.AbstractIterator;

public class BfsTreeIterator<T> extends AbstractIterator<T> {

	private final Deque<T> queue = new LinkedList<T>();

	private final TreeProvider<T> provider;

	private Iterator<T> currentIterator;

	public BfsTreeIterator(T root, TreeProvider<T> provider) {
		this.currentIterator = provider.listChildren(root);
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
			currentIterator = provider.listChildren(queue.pop());
			return getElement();
		}

		return null;
	}
}
