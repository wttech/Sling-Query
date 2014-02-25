package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.ArrayIterator;
import com.cognifide.sling.query.selector.parser.SelectorParser;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class SelectorFunction<T> implements IteratorToIteratorFunction<T>, Predicate<T> {

	private final List<IteratorToIteratorFunction<T>> functions;

	public SelectorFunction(List<SelectorSegment> segments, TreeProvider<T> provider,
			SearchStrategy strategy) {
		this.functions = createSegmentFunctions(segments, provider, strategy);
	}

	public static <T> SelectorFunction<T> parse(String selector, SearchStrategy strategy,
			TreeProvider<T> provider) {
		List<SelectorSegment> segments = SelectorParser.parse(selector);
		return new SelectorFunction<T>(segments, provider, strategy);
	}

	private List<IteratorToIteratorFunction<T>> createSegmentFunctions(List<SelectorSegment> segments,
			TreeProvider<T> provider, SearchStrategy strategy) {
		List<IteratorToIteratorFunction<T>> functions = new ArrayList<IteratorToIteratorFunction<T>>();
		for (SelectorSegment segment : segments) {
			functions.add(new SelectorSegmentFunction<T>(segment, provider, strategy));
		}
		return functions;
	}

	@Override
	public Iterator<T> apply(Iterator<T> input) {
		Iterator<T> result = input;
		for (IteratorToIteratorFunction<T> function : functions) {
			result = function.apply(result);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean accepts(T resource) {
		return apply(new ArrayIterator<T>(resource)).hasNext();
	}
}
