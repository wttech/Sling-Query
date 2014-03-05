package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.ReverseIterator;

public class NotFunction<T> implements IteratorToIteratorFunction<T> {

	private IteratorToIteratorFunction<T> function;

	public NotFunction(IteratorToIteratorFunction<T> function) {
		this.function = function;
	}

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		return new ReverseIterator<T>(function, input);
	}

}
