package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.tree.BfsTreeIterator;
import com.cognifide.sling.query.iterator.tree.DfsTreeIterator;
import com.cognifide.sling.query.iterator.tree.JcrTreeIterator;

public class FindFunction implements ResourceToIteratorFunction {

	private final String preFilteringSelector;

	private SearchStrategy strategy;

	public FindFunction(String preFilteringSelector, SearchStrategy searchStrategy) {
		this.preFilteringSelector = preFilteringSelector;
		this.strategy = searchStrategy;
	}

	@Override
	public Iterator<Resource> apply(Resource resource) {
		switch (strategy) {
			case BFS:
				return new BfsTreeIterator(resource);
			case JCR:
				return new JcrTreeIterator(preFilteringSelector, resource);
			case DFS:
			default:
				return new DfsTreeIterator(resource);
		}
	}
}