package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.tree.BfsTreeIterator;
import com.cognifide.sling.query.iterator.tree.DfsTreeIterator;
import com.cognifide.sling.query.iterator.tree.JcrTreeIterator;

public class FindFunction implements ResourceToIteratorFunction {

	private final SearchStrategy type;

	private final String selector;

	public FindFunction() {
		this(null, SearchStrategy.DFS);
	}

	public FindFunction(String selector, SearchStrategy type) {
		this.type = type;
		this.selector = selector;
	}

	@Override
	public Iterator<Resource> apply(Resource resource) {
		switch (type) {
			case BFS:
				return new BfsTreeIterator(resource);
			case JCR:
				return new JcrTreeIterator(selector, resource);
			case DFS:
			default:
				return new DfsTreeIterator(resource);
		}
	}
}