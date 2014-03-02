package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.selector.Option;

public class IdentityFunction<T> implements OptionIteratorToIteratorFunction<T> {

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> input) {
		return input;
	}

}
