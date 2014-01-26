package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.TreeIteratorType;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.tree.BfsTreeIterator;
import com.cognifide.sling.query.iterator.tree.DfsTreeIterator;

public class FindFunction implements ResourceToIteratorFunction {

	private final TreeIteratorType type;

	public FindFunction() {
		this(TreeIteratorType.DFS);
	}

	public FindFunction(TreeIteratorType type) {
		this.type = type;
	}

	@Override
	public Iterator<Resource> apply(Resource resource) {
		switch (type) {
			case BFS:
				return new BfsTreeIterator(resource);
			case DFS:
			default:
				return new DfsTreeIterator(resource);
		}
	}
}