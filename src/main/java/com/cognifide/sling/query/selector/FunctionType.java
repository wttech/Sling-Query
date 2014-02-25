package com.cognifide.sling.query.selector;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;
import com.cognifide.sling.query.function.EvenFunction;
import com.cognifide.sling.query.function.HasFunction;
import com.cognifide.sling.query.function.LastFunction;
import com.cognifide.sling.query.function.NotFunction;
import com.cognifide.sling.query.function.SliceFunction;

public enum FunctionType {
	EQ {
		@Override
		public <T> Function<?, ?> getFunction(String argument, SearchStrategy strategy,
				TreeProvider<T> providerw) {
			int index = Integer.parseInt(argument);
			return new SliceFunction<T>(index, index);
		}
	},
	FIRST {
		@Override
		public <T> Function<?, ?> getFunction(String argument, SearchStrategy strategy,
				TreeProvider<T> provider) {
			return new SliceFunction<T>(0, 0);
		}
	},
	LAST {
		@Override
		public <T> Function<?, ?> getFunction(String argument, SearchStrategy strategy,
				TreeProvider<T> provider) {
			return new LastFunction<T>();
		}
	},
	GT {
		@Override
		public <T> Function<?, ?> getFunction(String argument, SearchStrategy strategy,
				TreeProvider<T> provider) {
			return new SliceFunction<T>(Integer.valueOf(argument) + 1);
		}
	},
	LT {
		@Override
		public <T> Function<?, ?> getFunction(String argument, SearchStrategy strategy,
				TreeProvider<T> provider) {
			return new SliceFunction<T>(0, Integer.valueOf(argument) - 1);
		}
	},
	HAS {
		@Override
		public <T> Function<?, ?> getFunction(String selector, SearchStrategy strategy,
				TreeProvider<T> provider) {
			return new HasFunction<T>(selector, strategy, provider);
		}
	},
	PARENT {
		@Override
		public <T> Function<?, ?> getFunction(String selector, SearchStrategy strategy,
				final TreeProvider<T> provider) {
			return new ResourceToResourceFunction<T>() {
				@Override
				public T apply(T resource) {
					if (provider.listChildren(resource).hasNext()) {
						return resource;
					} else {
						return null;
					}
				}
			};
		}
	},
	EMPTY {
		@Override
		public <T> Function<?, ?> getFunction(String argument, SearchStrategy strategy,
				final TreeProvider<T> provider) {
			return new ResourceToResourceFunction<T>() {
				@Override
				public T apply(T resource) {
					if (provider.listChildren(resource).hasNext()) {
						return null;
					}
					return resource;
				}
			};
		}
	},
	ODD {
		@Override
		public <T> Function<?, ?> getFunction(String argument, SearchStrategy strategy,
				TreeProvider<T> provider) {
			return new EvenFunction<T>(false);
		}
	},
	EVEN {
		@Override
		public <T> Function<?, ?> getFunction(String argument, SearchStrategy strategy,
				TreeProvider<T> provider) {
			return new EvenFunction<T>(true);
		}
	},
	NOT {
		@Override
		public <T> Function<?, ?> getFunction(String argument, SearchStrategy strategy,
				TreeProvider<T> provider) {
			return new NotFunction<T>(SelectorFunction.parse(argument, strategy, provider));
		}
	};

	public abstract <T> Function<?, ?> getFunction(String argument, SearchStrategy strategy,
			TreeProvider<T> provider);
}
