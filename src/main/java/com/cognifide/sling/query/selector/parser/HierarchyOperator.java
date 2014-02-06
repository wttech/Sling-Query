package com.cognifide.sling.query.selector.parser;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.function.ChildrenFunction;
import com.cognifide.sling.query.function.FindFunction;
import com.cognifide.sling.query.function.NextFunction;
import com.cognifide.sling.query.predicate.RejectingPredicate;

public enum HierarchyOperator {
//@formatter:off
	CHILD('>') {
		@Override
		public Function<?, ?> getFunction(SearchStrategy strategy) {
			return new ChildrenFunction();
		}
	},
	DESCENDANT((char) 0) {
		@Override
		public Function<?, ?> getFunction(SearchStrategy strategy) {
			return new FindFunction("", strategy);
		}
	},
	NEXT_ADJACENT('+') {
		@Override
		public Function<?, ?> getFunction(SearchStrategy strategy) {
			return new NextFunction(null);
		}
	},
	NEXT_SIBLINGS('~') {
		@Override
		public Function<?, ?> getFunction(SearchStrategy strategy) {
			return new NextFunction(new RejectingPredicate());
		}
	};
//@formatter:on

	private final char c;

	private HierarchyOperator(char c) {
		this.c = c;
	}

	public abstract Function<?, ?> getFunction(SearchStrategy strategy);

	public static HierarchyOperator findByCharacter(char c) {
		for (HierarchyOperator operator : values()) {
			if (operator.c == c) {
				return operator;
			}
		}
		return DESCENDANT;
	}

}
