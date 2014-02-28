package com.cognifide.sling.query.function;

import com.cognifide.sling.query.api.function.ElementToElementFunction;

public class IdentityFunction<T> implements ElementToElementFunction<T> {

	@Override
	public T apply(T element) {
		return element;
	}

}
