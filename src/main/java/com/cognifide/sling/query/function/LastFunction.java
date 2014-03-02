package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.LastIterator;

public class LastFunction<T> implements OptionIteratorToIteratorFunction<T> {

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		return new LastIterator<T>(input);
	}

}
