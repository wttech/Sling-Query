package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.ListIterator;

import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.api.function.Option;

public class LastIterator<T> extends AbstractIterator<Option<T>> {

	private final LazyList<Option<T>> lazyList;

	private final ListIterator<Option<T>> iterator;

	private boolean finished;

	private boolean initialized;

	private int lastIndex = -1;

	public LastIterator(Iterator<Option<T>> iterator) {
		this.lazyList = new LazyList<Option<T>>(iterator);
		this.iterator = lazyList.listIterator();
	}

	@Override
	protected Option<T> getElement() {
		if (finished) {
			return null;
		}

		initializeLastIndex();

		Option<T> candidate;
		if (iterator.hasNext()) {
			candidate = iterator.next();
		} else {
			finished = true;
			return null;
		}
		if (iterator.previousIndex() == lastIndex) {
			finished = true;
			return candidate;
		} else {
			return Option.empty(candidate.getArgumentId());
		}
	}

	private void initializeLastIndex() {
		ListIterator<Option<T>> i = lazyList.listIterator();
		if (!initialized) {
			while (i.hasNext()) {
				if (!i.next().isEmpty()) {
					lastIndex = i.previousIndex();
				}
			}
		}
		initialized = true;
	}
}
