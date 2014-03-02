package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.selector.Option;

public class OptionFunctionIterator<T> extends AbstractIterator<Option<T>> {

	private final ElementToIteratorFunction<T> function;

	private final Iterator<Option<T>> parentIterator;

	private Iterator<T> currentIterator;

	public OptionFunctionIterator(ElementToIteratorFunction<T> function, Iterator<Option<T>> parentIterator) {
		this.function = function;
		this.parentIterator = parentIterator;
	}

	@Override
	protected Option<T> getElement() {
		if (currentIterator != null && currentIterator.hasNext()) {
			return new Option<T>(currentIterator.next());
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
				return new Option<T>();
			}
		}
		return null;
	}
}
