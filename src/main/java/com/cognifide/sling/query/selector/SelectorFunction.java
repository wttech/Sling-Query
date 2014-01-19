package com.cognifide.sling.query.selector;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;
import com.cognifide.sling.query.function.EvenFunction;
import com.cognifide.sling.query.function.HasFunction;
import com.cognifide.sling.query.function.LastFunction;
import com.cognifide.sling.query.function.SliceFunction;

public enum SelectorFunction {
	EQ {
		@Override
		public Function<?, ?> getFunction(String argument) {
			int index = Integer.parseInt(argument);
			return new SliceFunction(index, index);
		}
	},
	FIRST {
		@Override
		public Function<?, ?> getFunction(String argument) {
			return new SliceFunction(0, 0);
		}
	},
	LAST {
		@Override
		public Function<?, ?> getFunction(String argument) {
			return new LastFunction();
		}
	},
	GT {
		@Override
		public Function<?, ?> getFunction(String argument) {
			return new SliceFunction(Integer.valueOf(argument) + 1);
		}
	},
	LT {
		@Override
		public Function<?, ?> getFunction(String argument) {
			return new SliceFunction(0, Integer.valueOf(argument) - 1);
		}
	},
	HAS {
		@Override
		public Function<?, ?> getFunction(String selector) {
			return new HasFunction(new Selector(selector).getPredicate());
		}
	},
	PARENT {
		@Override
		public Function<?, ?> getFunction(String selector) {
			return new ResourceToResourceFunction() {
				@Override
				public Resource apply(Resource resource) {
					if (resource.listChildren().hasNext()) {
						return resource;
					} else {
						return null;
					}
				}
			};
		}
	},
	ODD {
		@Override
		public Function<?, ?> getFunction(String argument) {
			return new EvenFunction(false);
		}
	},
	EVEN {
		@Override
		public Function<?, ?> getFunction(String argument) {
			return new EvenFunction(true);
		}
	};

	public abstract Function<?, ?> getFunction(String argument);
}
