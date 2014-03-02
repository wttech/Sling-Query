package com.cognifide.sling.query.function;

import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.EmptyElementFilter;
import com.cognifide.sling.query.iterator.OptionalElementIterator;
import com.cognifide.sling.query.selector.Option;

public class CompositeFunction<T> {
	private final List<OptionIteratorToIteratorFunction<T>> functions;

	public CompositeFunction(List<OptionIteratorToIteratorFunction<T>> functions) {
		this.functions = functions;
	}

	public Iterator<T> apply(Iterator<T> input) {
		Iterator<Option<T>> result = new OptionalElementIterator<T>(input);
		for (OptionIteratorToIteratorFunction<T> f : functions) {
			result = f.apply(result);
		}
		return new EmptyElementFilter<T>(result);
	}
}
