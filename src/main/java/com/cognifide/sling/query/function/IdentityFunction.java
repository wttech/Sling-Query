package com.cognifide.sling.query.function;

import com.cognifide.sling.query.api.function.ResourceToResourceFunction;

public class IdentityFunction<T> implements ResourceToResourceFunction<T> {

	@Override
	public T apply(T element) {
		return element;
	}

}
