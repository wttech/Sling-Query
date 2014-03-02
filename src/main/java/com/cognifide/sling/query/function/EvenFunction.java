package com.cognifide.sling.query.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.FilteringIterator;
import com.cognifide.sling.query.selector.Option;

public class EvenFunction<T> implements OptionIteratorToIteratorFunction<T> {

	private final boolean even;

	public EvenFunction(boolean even) {
		this.even = even;
	}

	@Override
	public Iterator<Option<T>> apply(Iterator<Option<T>> resources) {
		return new FilteringIterator<T>(resources, new EvenPredicate<T>(even));
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
