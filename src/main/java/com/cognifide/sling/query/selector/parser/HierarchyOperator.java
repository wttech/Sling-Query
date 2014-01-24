package com.cognifide.sling.query.selector.parser;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.function.ChildrenFunction;
import com.cognifide.sling.query.function.FindFunction;
import com.cognifide.sling.query.function.NextFunction;
import com.cognifide.sling.query.predicate.RejectingPredicate;

public enum HierarchyOperator {
//@formatter:off
	CHILD('>', new ChildrenFunction()),
	DESCENDANT((char) 0, new FindFunction()),
	NEXT_ADJACENT('+', new NextFunction(null)),
	NEXT_SIBLINGS('~', new NextFunction(new RejectingPredicate()));
//@formatter:on

	private final char c;

	private final Function<?, ?> function;

	private HierarchyOperator(char c, Function<?, ?> function) {
		this.c = c;
		this.function = function;
	}

	public Function<?, ?> getFunction() {
		return function;
	}

	public static HierarchyOperator findByCharacter(char c) {
		for (HierarchyOperator operator : values()) {
			if (operator.c == c) {
				return operator;
			}
		}
		return DESCENDANT;
	}

}
