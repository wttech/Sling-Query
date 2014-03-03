package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;

public class ReverseIterator<T> extends AbstractIterator<Option<T>> {

	private final Iterator<Option<T>> filtered;

	private final Iterator<Option<T>> original;

	public ReverseIterator(IteratorToIteratorFunction<T> function, Iterator<Option<T>> input) {
		LazyList<Option<T>> lazyList = new LazyList<Option<T>>(input);
		filtered = function.apply(lazyList.listIterator());
		original = lazyList.listIterator();
	}

	@Override
	protected Option<T> getElement() {
		if (original.hasNext()) {
			Option<T> originalElement = original.next();
			Option<T> filteredElement = Option.empty();
			if (filtered.hasNext()) {
				filteredElement = filtered.next();
			}
			if (filteredElement.isEmpty()) {
				return originalElement;
			} else {
				return Option.empty();
			}
		}
		return null;
	}
}
