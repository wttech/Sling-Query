package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.function.Option;

public class AlternativeIterator<T> extends AbstractIterator<Option<T>> {

	private final List<Iterator<Option<T>>> iterators;

	private static AlternativeIterator<?> iterator;

	private static int i = 0;

	public AlternativeIterator(List<Iterator<Option<T>>> iterators) {
		this.iterators = iterators;
		if (i-- == 0) {
			iterator = this;
		}
	}

	@Override
	protected Option<T> getElement() {
		Option<T> element = null;
		if (iterator == this) {
			System.out.println("getElement");
		}
		for (Iterator<Option<T>> i : iterators) {
			if (i.hasNext()) {
				if (iterator == this) {
					System.out.println("iterator " + i);
				}
				Option<T> option = i.next();
				if (element == null || !option.isEmpty()) {
					if (iterator == this) {
						System.out.println("element set to " + option);
					}
					element = option;
				}
			}
		}
		if (iterator == this) {
			System.out.println("result: " + element);
			System.out.println();
		}
		return element;
	}
}