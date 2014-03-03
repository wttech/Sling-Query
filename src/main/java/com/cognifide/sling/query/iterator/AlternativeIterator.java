package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.function.Option;

public class AlternativeIterator<T> extends AbstractIterator<Option<T>> {

	private final List<Iterator<Option<T>>> iterators;

	public AlternativeIterator(List<Iterator<Option<T>>> iterators) {
		this.iterators = iterators;
	}

	@Override
	protected Option<T> getElement() {
		Option<T> element = null;
		for (Iterator<Option<T>> i : iterators) {
			if (i.hasNext()) {
				Option<T> option = i.next();
				if (element == null || !option.isEmpty()) {
					element = option;
				}
			}
		}
		return element;
	}
}