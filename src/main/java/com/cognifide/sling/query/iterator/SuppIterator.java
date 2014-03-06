package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;

/**
 * This class modifies the input iterator, removing all elements that are mapped to an empty value by the
 * function.
 */
public class SuppIterator<T> extends AbstractIterator<Option<T>> {

	private final List<Option<T>> input;

	private final Iterator<Option<T>> output;

	private Option<T> outputElement;

	private int currentIndex = 0;

	public SuppIterator(List<Option<T>> input, IteratorToIteratorFunction<T> function) {
		this.input = input;
		this.output = function.apply(new ArgumentResettingIterator<T>(input.iterator()));
	}

	@Override
	protected Option<T> getElement() {
		if (outputElement != null) {
			final int outputIndex = outputElement.getArgumentId();
			if (currentIndex < outputIndex) {
				return Option.empty(input.get(currentIndex++).getArgumentId());
			} else if (currentIndex == outputIndex && !outputElement.isEmpty()) {
				return input.get(currentIndex++);
			}
		}

		while (output.hasNext()) {
			outputElement = output.next();
			final int outputIndex = outputElement.getArgumentId();
			if ((outputIndex == currentIndex && !outputElement.isEmpty()) || outputIndex > currentIndex) {
				return getElement();
			}
		}
		return null;
	}
}
