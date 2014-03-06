package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;

/**
 * This iterator returns all elements of the input list which are mapped to non-empty values by the input
 * function. Name is inspired by the <a href="http://en.wikipedia.org/wiki/Support_(mathematics)">support of
 * the function</a>.
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

	/**
	 * The idea behind this method is that index of each element in the input iterator is passed to the
	 * function. Elements returned by the output iterator contains the same index, which can be used to assign
	 * input to output elements. We check which indices are present in the output iterator and return only
	 * related input elements.
	 */
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
