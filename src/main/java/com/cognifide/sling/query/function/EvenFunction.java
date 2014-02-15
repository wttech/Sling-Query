package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;

public class EvenFunction implements IteratorToIteratorFunction {

	private final boolean even;

	public EvenFunction(boolean even) {
		this.even = even;
	}

	@Override
	public Iterator<Resource> apply(Iterator<Resource> resources) {
		return new FilteringIteratorWrapper<Resource>(resources, new EvenPredicate<Resource>(even));
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
