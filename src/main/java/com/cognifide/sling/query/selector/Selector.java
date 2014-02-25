package com.cognifide.sling.query.selector;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cognifide.sling.query.TreeStructureProvider;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.predicate.FunctionPredicate;
import com.cognifide.sling.query.selector.parser.SelectorParser;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class Selector<T> implements IteratorToIteratorFunction<T> {

	private final List<SelectorSegment<T>> segments;

	public Selector(String selectorString, SearchStrategy strategy, TreeStructureProvider<T> provider) {
		if (StringUtils.isBlank(selectorString)) {
			segments = Collections.emptyList();
		} else {
			segments = SelectorParser.parse(selectorString, strategy, provider).getSegments();
		}
	}

	@Override
	public Iterator<T> apply(Iterator<T> input) {
		Iterator<T> iterator = input;
		for (SelectorSegment<T> segment : segments) {
			iterator = segment.apply(iterator);
		}
		return iterator;
	}

	public Predicate<T> asPredicate() {
		return new FunctionPredicate<T>(this);
	}
}
