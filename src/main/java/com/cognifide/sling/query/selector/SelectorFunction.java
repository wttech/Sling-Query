package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.CompositeIterator;
import com.cognifide.sling.query.iterator.EmptyElementFilter;
import com.cognifide.sling.query.selector.parser.SelectorParser;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class SelectorFunction<T> implements OptionIteratorToIteratorFunction<T>, Predicate<T> {

	private final List<Function<?, ?>> functions;

	public SelectorFunction(List<SelectorSegment> segments, TreeProvider<T> provider, SearchStrategy strategy) {
		this.functions = createSegmentFunctions(segments, provider, strategy);
	}

	public static <T> SelectorFunction<T> parse(String selector, SearchStrategy strategy,
			TreeProvider<T> provider) {
		List<SelectorSegment> segments = SelectorParser.parse(selector).get(0).getSegments();
		return new SelectorFunction<T>(segments, provider, strategy);
	}

	private List<Function<?, ?>> createSegmentFunctions(List<SelectorSegment> segments,
			TreeProvider<T> provider, SearchStrategy strategy) {
		List<Function<?, ?>> functions = new ArrayList<Function<?, ?>>();
		for (SelectorSegment segment : segments) {
			functions.addAll(SelectorSegmentFunction.createFunctions(segment, provider, strategy));
		}
		return functions;
	}

	public Iterator<Option<T>> apply(ListIterator<T> input) {
		return new CompositeIterator<T>(input, functions);
	}

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		return apply(new LazyList<T>(new EmptyElementFilter<T>(input)).listIterator());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean accepts(T resource) {
		return new EmptyElementFilter<T>(apply(Arrays.asList(resource).listIterator())).hasNext();
	}
}
