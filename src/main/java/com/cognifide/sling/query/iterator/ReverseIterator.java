package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.selector.SelectorFunction;

public class ReverseIterator<T> extends AbstractIterator<Option<T>> {

	private final Iterator<Option<T>> filtered;

	private final Iterator<Option<T>> original;

	public ReverseIterator(SelectorFunction<T> selector, Iterator<Option<T>> input) {
		LazyList<Option<T>> lazyList = new LazyList<Option<T>>(input);
		filtered = selector.apply(lazyList.listIterator());
		original = lazyList.listIterator();
	}

	@Override
	protected Option<T> getElement() {
		if (filtered.hasNext() && original.hasNext()) {
			Option<T> originalElement = original.next();
			Option<T> filteredElement = filtered.next();
			if (filteredElement.isEmpty()) {
				return originalElement;
			} else {
				return Option.empty();
			}
		}
		return null;
	}

}
