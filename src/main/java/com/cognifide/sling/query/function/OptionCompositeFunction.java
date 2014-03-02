package com.cognifide.sling.query.function;

import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.IteratorFactory;
import com.cognifide.sling.query.selector.Option;

public class OptionCompositeFunction<T> implements OptionIteratorToIteratorFunction<T> {

	private final List<Function<?, ?>> functions;

	public OptionCompositeFunction(List<Function<?, ?>> functions) {
		this.functions = functions;
	}

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		Iterator<Option<T>> iterator = input;
		for (Function<?, ?> f : functions) {
			iterator = IteratorFactory.getOptionIterator(f, iterator);
		}
		return iterator;
	}
}
