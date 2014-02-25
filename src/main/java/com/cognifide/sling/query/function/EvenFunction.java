package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;

public class EvenFunction<T> implements IteratorToIteratorFunction<T> {

	private final boolean even;

	public EvenFunction(boolean even) {
		this.even = even;
	}

	@Override
	public Iterator<T> apply(Iterator<T> resources) {
		return new FilteringIteratorWrapper<T>(resources, new EvenPredicate<T>(even));
	}

	private static class EvenPredicate<T> implements Predicate<T> {
		private boolean accept;

		public EvenPredicate(boolean firstState) {
			accept = firstState;
		}

		@Override
		public boolean accepts(T element) {
			boolean oldAccept = accept;
			accept = !accept;
			return oldAccept;
		}
	}
}
