package com.cognifide.sling.query.api;

public interface Function<F, T> {
	T apply(F input);
}
