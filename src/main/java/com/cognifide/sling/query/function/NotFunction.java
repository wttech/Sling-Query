package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.EmptyElementFilter;
import com.cognifide.sling.query.iterator.ReverseIterator;
import com.cognifide.sling.query.selector.SelectorFunction;

public class NotFunction<T> implements IteratorToIteratorFunction<T> {

	private SelectorFunction<T> selector;

	public NotFunction(SelectorFunction<T> selector) {
		this.selector = selector;
	}

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		Iterator<Option<T>> filtered = new EmptyElementFilter<T>(input);
		return new ReverseIterator<T>(selector, filtered);
	}

}
