package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.api.function.Option;

/**
 * This iterator evaluates each element from the source iterator, expanding it using given function.
 */
public class ExpandingIterator<T> extends AbstractIterator<Option<T>> {

	private final ElementToIteratorFunction<T> function;

	private final Iterator<Option<T>> parentIterator;

	private Iterator<T> currentIterator;

	public ExpandingIterator(ElementToIteratorFunction<T> expandingFunction,
			Iterator<Option<T>> sourceIterator) {
		this.function = expandingFunction;
		this.parentIterator = sourceIterator;
	}

	@Override
	protected Option<T> getElement() {
		if (currentIterator != null && currentIterator.hasNext()) {
			return Option.of(currentIterator.next());
		}
		while (parentIterator.hasNext()) {
			Option<T> parentElement = parentIterator.next();
			if (parentElement.isEmpty()) {
				return parentElement;
			}
			currentIterator = function.apply(parentElement.getElement());
			if (currentIterator.hasNext()) {
				return getElement();
			} else {
				return Option.empty();
			}
		}
		return null;
	}
}
