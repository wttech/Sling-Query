package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.cognifide.sling.query.IteratorUtils;
import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.function.CompositeFunction;
import com.cognifide.sling.query.function.FilterFunction;
import com.cognifide.sling.query.iterator.NondegenerateIterator;
import com.cognifide.sling.query.iterator.EmptyElementFilter;
import com.cognifide.sling.query.selector.parser.Modifier;
import com.cognifide.sling.query.selector.parser.SelectorParser;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class SelectorFunction<T> implements OptionIteratorToIteratorFunction<T>, Predicate<T> {

	private final OptionIteratorToIteratorFunction<T> function;

	public SelectorFunction(List<SelectorSegment> segments, TreeProvider<T> provider, SearchStrategy strategy) {
		this.function = createFunction(segments, provider, strategy);
	}

	public static <T> SelectorFunction<T> parse(String selector, SearchStrategy strategy,
			TreeProvider<T> provider) {
		List<SelectorSegment> segments = SelectorParser.parse(selector).get(0).getSegments();
		return new SelectorFunction<T>(segments, provider, strategy);
	}

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		LazyList<Option<T>> list = new LazyList<Option<T>>(input);
		return applyListIterator(list.listIterator());
	}

	@Override
	public boolean accepts(T resource) {
		Iterator<Option<T>> result = apply(IteratorUtils.singleElementIterator(Option.of(resource)));
		return new EmptyElementFilter<T>(result).hasNext();
	}

	public Iterator<Option<T>> applyListIterator(ListIterator<Option<T>> input) {
		return new NondegenerateIterator<T>(input, function);
	}

	private OptionIteratorToIteratorFunction<T> createFunction(List<SelectorSegment> segments,
			TreeProvider<T> provider, SearchStrategy strategy) {
		List<Function<?, ?>> functions = new ArrayList<Function<?, ?>>();
		for (SelectorSegment segment : segments) {
			functions.addAll(createSegmentFunction(segment, provider, strategy));
		}
		return new CompositeFunction<T>(functions);
	}

	public static <T> List<Function<?, ?>> createSegmentFunction(SelectorSegment segment,
			TreeProvider<T> provider, SearchStrategy strategy) {
		List<Function<?, ?>> functions = new ArrayList<Function<?, ?>>();
		HierarchyOperator operator = HierarchyOperator.findByCharacter(segment.getHierarchyOperator());
		functions.add(operator.getFunction(strategy, provider));
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
