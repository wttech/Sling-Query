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
	};

	public abstract IteratorToIteratorFunction getFunction(String argument);
}
