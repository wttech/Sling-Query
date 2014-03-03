package com.cognifide.sling.query.resource.jcr.query;

import java.util.Iterator;
import java.util.List;

public class Formula implements Term {
	public enum Operator {
		AND, OR
	}

	private final Operator operator;

	private final List<Term> conditions;

	public Formula(Operator operator, List<Term> conditions) {
		this.operator = operator;
		this.conditions = conditions;
	}

	public String buildString() {
		if (conditions.isEmpty()) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		Iterator<Term> iterator = conditions.iterator();
		if (conditions.size() > 1) {
			builder.append("(");
		}
		while (iterator.hasNext()) {
			Term term = iterator.next();
			builder.append(term.buildString());
			if (iterator.hasNext()) {
				builder.append(' ').append(operator.toString()).append(' ');
			}
		}
		if (conditions.size() > 1) {
			builder.append(")");
		}
		return builder.toString();
	}
}
