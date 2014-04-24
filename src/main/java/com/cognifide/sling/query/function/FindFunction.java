package com.cognifide.sling.query.function;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.iterator.tree.BfsTreeIterator;
import com.cognifide.sling.query.iterator.tree.DfsTreeIterator;
import com.cognifide.sling.query.selector.parser.Selector;
import com.cognifide.sling.query.selector.parser.SelectorParser;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class FindFunction<T> implements ElementToIteratorFunction<T> {

	private final List<SelectorSegment> preFilteringSelector;

	private final TreeProvider<T> provider;

	private final SearchStrategy strategy;

	public FindFunction(SearchStrategy searchStrategy, TreeProvider<T> provider,
			SelectorSegment preFilteringSelector) {
		this.strategy = searchStrategy;
		this.provider = provider;
		this.preFilteringSelector = Arrays.asList(preFilteringSelector);
	}

	public FindFunction(SearchStrategy searchStrategy, TreeProvider<T> provider, String preFilteringSelector) {
		this.strategy = searchStrategy;
		this.provider = provider;
		List<Selector> selectors = SelectorParser.parse(preFilteringSelector);
		this.preFilteringSelector = SelectorParser.getFirstSegmentFromEachSelector(selectors);
	}

	@Override
	public Iterator<T> apply(T input) {
		Iterator<T> iterator;
		switch (strategy) {
			case BFS:
				iterator = new BfsTreeIterator<T>(input, provider);
				break;
			case QUERY:
				iterator = provider.query(preFilteringSelector, input);
				break;
			case DFS:
			default:
				iterator = new DfsTreeIterator<T>(input, provider);
				break;
		}
		return new WarningIterator<T>(iterator);
	}
}