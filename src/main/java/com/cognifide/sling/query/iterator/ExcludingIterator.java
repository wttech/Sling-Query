package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.api.Function;

public class ExcludingIterator<T> extends AbstractIterator<T> {

	private final Iterator<T> iterator;

	private final Iterator<T> toExclude;

	private T nextToExclude;

	public ExcludingIterator(Iterator<T> iterator, Function<Iterator<T>, Iterator<T>> excludingFunction) {
		List<T> lazyList = new LazyList<T>(iterator);
		this.iterator = lazyList.iterator();
		this.toExclude = excludingFunction.apply(lazyList.iterator());
	}

	@Override
	protected T getElement() {
		T element = null;
		while (iterator.hasNext() && element == null) {
			if (nextToExclude == null && toExclude.hasNext()) {
				nextToExclude = toExclude.next();
			}
			T candidate = iterator.next();
			if (candidate.equals(nextToExclude)) {
				nextToExclude = null;
			} else {
				element = candidate;
			}
		}
		return element;
	}

}
