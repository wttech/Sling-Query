package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.ReverseIterator;
import com.cognifide.sling.query.selector.Option;
import com.cognifide.sling.query.selector.SelectorFunction;

public class NotFunction<T> implements OptionIteratorToIteratorFunction<T> {

	private SelectorFunction<T> selector;

	public NotFunction(SelectorFunction<T> selector) {
		this.selector = selector;
	}

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		return new ReverseIterator<T>(selector, input);
	}

}
