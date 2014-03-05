package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.IteratorUtils;
import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.function.CompositeFunction;
import com.cognifide.sling.query.function.FilterFunction;
import com.cognifide.sling.query.iterator.AlternativeIterator;
import com.cognifide.sling.query.iterator.SuppIterator;
import com.cognifide.sling.query.iterator.EmptyElementFilter;
import com.cognifide.sling.query.selector.parser.Modifier;
import com.cognifide.sling.query.selector.parser.Selector;
import com.cognifide.sling.query.selector.parser.SelectorParser;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class SelectorFunction<T> implements IteratorToIteratorFunction<T>, Predicate<T> {

	private final List<IteratorToIteratorFunction<T>> functions;

	private final TreeProvider<T> provider;

	private final SearchStrategy strategy;

	public SelectorFunction(String selector, TreeProvider<T> provider, SearchStrategy strategy) {
		this.provider = provider;
		this.strategy = strategy;
		List<Selector> selectors = SelectorParser.parse(selector);
		functions = new ArrayList<IteratorToIteratorFunction<T>>();
		for (Selector s : selectors) {
			functions.add(createSelectorFunction(s.getSegments()));
		}
	}

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		LazyList<Option<T>> list = new LazyList<Option<T>>(input);
		List<Iterator<Option<T>>> iterators = new ArrayList<Iterator<Option<T>>>();
		for (IteratorToIteratorFunction<T> function : functions) {
			iterators.add(new SuppIterator<T>(list, function));
		}
		return new AlternativeIterator<T>(iterators);
	}

	@Override
	public boolean accepts(T resource) {
		Iterator<Option<T>> result = apply(IteratorUtils.singleElementIterator(Option.of(resource, 0)));
		return new EmptyElementFilter<T>(result).hasNext();
	}

	private IteratorToIteratorFunction<T> createSelectorFunction(List<SelectorSegment> segments) {
		List<Function<?, ?>> segmentFunctions = new ArrayList<Function<?, ?>>();
		for (SelectorSegment segment : segments) {
			segmentFunctions.addAll(createSegmentFunction(segment));
		}
		return new CompositeFunction<T>(segmentFunctions);
	}

	private List<Function<?, ?>> createSegmentFunction(SelectorSegment segment) {
		List<Function<?, ?>> functions = new ArrayList<Function<?, ?>>();
		HierarchyOperator operator = HierarchyOperator.findByCharacter(segment.getHierarchyOperator());
		functions.add(operator.getFunction(segment, strategy, provider));
		Predicate<T> predicate = provider.getPredicate(segment.getType(), segment.getName(),
				segment.getAttributes());
		functions.add(new FilterFunction<T>(predicate));
		for (Modifier modifiers : segment.getModifiers()) {
			FunctionType type = FunctionType.valueOf(modifiers.getName().toUpperCase());
			Function<?, ?> f = type.getFunction(modifiers.getArgument(), strategy, provider);
			functions.add(f);
		}
		return functions;
	}

}
