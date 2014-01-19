package com.cognifide.sling.query.selector;

import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.function.SliceFunction;

public enum SelectorFunction {
	EQ {
		@Override
		public IteratorToIteratorFunction getFunction(String argument) {
			int index = Integer.parseInt(argument);
			return new SliceFunction(index, index);
		}
	},
	FIRST {
		@Override
		public IteratorToIteratorFunction getFunction(String argument) {
			return new SliceFunction(0, 0);
		}
	};

	public abstract IteratorToIteratorFunction getFunction(String argument);
}
