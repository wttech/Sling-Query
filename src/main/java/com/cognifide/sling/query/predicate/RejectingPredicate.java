package com.cognifide.sling.query.predicate;

import com.cognifide.sling.query.api.Predicate;

public class RejectingPredicate<T> implements Predicate<T> {

	private final Predicate<T> predicate;

	public RejectingPredicate() {
		this(new Predicate<T>() {
			@Override
			public boolean accepts(T resource) {
				return true;
			}
		});
	}

	public RejectingPredicate(Predicate<T> predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean accepts(T value) {
		return !predicate.accepts(value);
	}
}
