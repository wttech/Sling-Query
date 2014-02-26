package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.function.CompositeFunction;
import com.cognifide.sling.query.iterator.ArrayIterator;
import com.cognifide.sling.query.selector.parser.Selector;
import com.cognifide.sling.query.selector.parser.SelectorParser;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class SelectorFunction<T> implements IteratorToIteratorFunction<T>, Predicate<T> {

	private final List<IteratorToIteratorFunction<T>> functions;

	public SelectorFunction(List<Selector> selectors, TreeProvider<T> provider, SearchStrategy strategy) {
		this.functions = createSegmentFunctions(selectors, provider, strategy);
	}

	public static <T> SelectorFunction<T> parse(String selector, SearchStrategy strategy,
			TreeProvider<T> provider) {
		List<Selector> selectors = SelectorParser.parse(selector);
		return new SelectorFunction<T>(selectors, provider, strategy);
	}

	private List<IteratorToIteratorFunction<T>> createSegmentFunctions(List<Selector> selectors,
			TreeProvider<T> provider, SearchStrategy strategy) {
		List<IteratorToIteratorFunction<T>> functions = new ArrayList<IteratorToIteratorFunction<T>>();
		for (Selector selector : selectors) {
			List<IteratorToIteratorFunction<T>> segmentFunctions = new ArrayList<IteratorToIteratorFunction<T>>();
			for (SelectorSegment segment : selector.getSegments()) {
				functions.add(new SelectorSegmentFunction<T>(segment, provider, strategy));
			}
			functions.add(new CompositeFunction<T>(segmentFunctions));
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
