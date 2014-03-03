package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.ListIterator;

import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;

/**
 * This class modifies the input iterator, removing all elements that are mapped to an empty value by the
 * function.
 */
public class NondegenerateIterator<T> extends AbstractIterator<Option<T>> {

	private final ListIterator<Option<T>> input;

	private final Iterator<Option<T>> output;

	private int currentElement = 0;

	private boolean finished = false;

	public NondegenerateIterator(ListIterator<Option<T>> iterator, IteratorToIteratorFunction<T> function) {
		input = iterator;
		output = function.apply(iterator);
	}

	@Override
	protected Option<T> getElement() {
		if (finished) {
			return null;
		}

		boolean emptyResult = true;
		do {
			if (output.hasNext()) {
				emptyResult = output.next().isEmpty() && emptyResult;
			} else {
				// all remaining input elements should be mapped to `empty`
				if (input.hasNext()) {
					input.next();
					return Option.empty();
				} else {
					finished = true;
					return null;
				}
			}
		} while (currentElement == input.nextIndex());

		currentElement = input.previousIndex();
		return getResult(emptyResult);
	}

	private Option<T> getResult(boolean empty) {
		if (empty) {
			return Option.empty();
		} else {
			Option<T> previous = input.previous();
			input.next();
			return previous;
		}
	}
}
