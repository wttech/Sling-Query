package com.cognifide.sling.query.function;

import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.iterator.tree.BfsTreeIterator;
import com.cognifide.sling.query.iterator.tree.DfsTreeIterator;
import com.cognifide.sling.query.selector.parser.SelectorParser;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class FindFunction<T> implements ElementToIteratorFunction<T> {

	private final SelectorSegment preFilteringSelector;

	private final TreeProvider<T> provider;

	private final SearchStrategy strategy;

	public FindFunction(SearchStrategy searchStrategy, TreeProvider<T> provider,
			SelectorSegment preFilteringSelector) {
		this.strategy = searchStrategy;
		this.provider = provider;
		this.preFilteringSelector = preFilteringSelector;
	}

	public FindFunction(SearchStrategy searchStrategy, TreeProvider<T> provider, String preFilteringSelector) {
		this.strategy = searchStrategy;
		this.provider = provider;
		List<SelectorSegment> segments = SelectorParser.parse(preFilteringSelector).get(0).getSegments();
		if (segments.isEmpty()) {
			this.preFilteringSelector = null;
		} else {
			this.preFilteringSelector = segments.get(0);
		}
	}

	@Override
	public Iterator<T> apply(T resource) {
		Iterator<T> iterator;
		switch (strategy) {
			case BFS:
				iterator = new BfsTreeIterator<T>(resource, provider);
				break;
			case QUERY:
				iterator = provider.query(preFilteringSelector, resource);
				break;
			case DFS:
			default:
				iterator = new DfsTreeIterator<T>(resource, provider);
				break;
		}
		return iterator;
	}
}