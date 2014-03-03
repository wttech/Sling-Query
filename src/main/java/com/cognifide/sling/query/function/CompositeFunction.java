package com.cognifide.sling.query.function;

import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;

public class CompositeFunction<T> implements IteratorToIteratorFunction<T> {

	private final List<Function<?, ?>> functions;

	public CompositeFunction(List<Function<?, ?>> functions) {
		this.functions = functions;
	}

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		Iterator<Option<T>> iterator = input;
		for (Function<?, ?> f : functions) {
			iterator = new IteratorToIteratorFunctionWrapper<T>(f).apply(iterator);
		}
		return iterator;
	}
}
