package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.function.FilterFunction;
import com.cognifide.sling.query.iterator.IteratorFactory;
import com.cognifide.sling.query.selector.parser.Modifier;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class SelectorSegmentFunction<T> implements IteratorToIteratorFunction<T> {

	private final List<Function<?, ?>> functions;

	public SelectorSegmentFunction(SelectorSegment segment, TreeProvider<T> provider, SearchStrategy strategy) {
		this.functions = createFunctions(segment, provider, strategy);
	}

	private static <T> List<Function<?, ?>> createFunctions(SelectorSegment segment,
			TreeProvider<T> provider, SearchStrategy strategy) {
		List<Function<?, ?>> functions = new ArrayList<Function<?, ?>>();
		HierarchyOperator operator = HierarchyOperator.findByCharacter(segment.getHierarchyOperator());
		Predicate<T> predicate = provider.getPredicate(segment.getType(), segment.getName(),
				segment.getAttributes());
		functions.add(operator.getFunction(strategy, provider));
		functions.add(new FilterFunction<T>(predicate));
		for (Modifier modifiers : segment.getModifiers()) {
			FunctionType type = FunctionType.valueOf(modifiers.getName().toUpperCase());
			Function<?, ?> f = type.getFunction(modifiers.getArgument(), strategy, provider);
			functions.add(f);
		}
		return functions;
	}

	@Override
	public Iterator<T> apply(Iterator<T> input) {
		Iterator<T> result = input;
		for (Function<?, ?> f : functions) {
			result = IteratorFactory.getIterator(f, result);
		}
		return result;
	}
}
