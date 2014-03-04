package com.cognifide.sling.query.predicate;

import com.cognifide.sling.query.api.Predicate;

public class RejectingPredicate<T> implements Predicate<T> {

	@Override
	public boolean accepts(T value) {
		return false;
	}

}
