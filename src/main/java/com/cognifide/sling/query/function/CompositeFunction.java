package com.cognifide.sling.query.function;

import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;

public class CompositeFunction<T> implements IteratorToIteratorFunction<T> {
	private final List<IteratorToIteratorFunction<T>> functions;

	public CompositeFunction(List<IteratorToIteratorFunction<T>> functions) {
		this.functions = functions;
	}

	@Override
	public Iterator<T> apply(Iterator<T> input) {
		Iterator<T> result = input;
		for (IteratorToIteratorFunction<T> f : functions) {
			result = f.apply(result);
		}
		return result;
	}
}
