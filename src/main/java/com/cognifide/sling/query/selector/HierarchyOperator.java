package com.cognifide.sling.query.selector;

import com.cognifide.sling.query.TreeProvider;
import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.function.ChildrenFunction;
import com.cognifide.sling.query.function.FindFunction;
import com.cognifide.sling.query.function.IdentityFunction;
import com.cognifide.sling.query.function.NextFunction;
import com.cognifide.sling.query.predicate.RejectingPredicate;

public enum HierarchyOperator {
//@formatter:off
	CHILD('>') {
		@Override
		public <T> Function<?, ?> getFunction(SearchStrategy strategy, TreeProvider<T> provider) {
			return new ChildrenFunction<T>(provider);
		}
	},
	DESCENDANT(' ') {
		@Override
		public <T> Function<?, ?> getFunction(SearchStrategy strategy, TreeProvider<T> provider) {
			return new FindFunction<T>("", strategy, provider);
		}
	},
	NEXT_ADJACENT('+') {
		@Override
		public <T> Function<?, ?> getFunction(SearchStrategy strategy, TreeProvider<T> provider) {
			return new NextFunction<T>(null, provider);
		}
	},
	NEXT_SIBLINGS('~') {
		@Override
		public <T> Function<?, ?> getFunction(SearchStrategy strategy, TreeProvider<T> provider) {
			return new NextFunction<T>(new RejectingPredicate<T>(), provider);
		}
	},
	NOOP((char)0) {
		@Override
		public <T> Function<?, ?> getFunction(SearchStrategy strategy, TreeProvider<T> provider) {
			return new IdentityFunction<T>();
		}
	};
//@formatter:on

	private final char c;

	private HierarchyOperator(char c) {
		this.c = c;
	}

	public abstract <T> Function<?, ?> getFunction(SearchStrategy strategy, TreeProvider<T> provider);

	public static HierarchyOperator findByCharacter(char c) {
		for (HierarchyOperator operator : values()) {
			if (operator.c == c) {
				return operator;
			}
		}
		return DESCENDANT;
	}

}
