package com.cognifide.sling.query.resource.jcr.query;

public class Atomic implements Term {

	private final String condition;

	public Atomic(String condition) {
		this.condition = condition;
	}

	@Override
	public String buildString() {
		return condition;
	}
}
