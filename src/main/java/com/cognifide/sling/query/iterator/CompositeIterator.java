package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.function.OptionCompositeFunction;
import com.cognifide.sling.query.selector.Option;

public class CompositeIterator<T> extends AbstractIterator<Option<T>> {

	private final ListIterator<T> input;

	private final Iterator<Option<T>> output;

	private int currentElement = 0;

	private boolean finished = false;

	public CompositeIterator(ListIterator<T> iterator, List<Function<?, ?>> functions) {
		this.input = iterator;
		OptionIteratorToIteratorFunction<T> function = new OptionCompositeFunction<T>(functions);
		output = function.apply(new OptionalElementIterator<T>(iterator));
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
				finished = true;
				break;
			}
		} while (currentElement == input.nextIndex());

		currentElement = input.previousIndex();
		Option<T> result = getResult(emptyResult);
		return result;
	}

	private Option<T> getResult(boolean empty) {
		if (empty) {
			return new Option<T>();
		} else {
			T previous = input.previous();
			input.next();
			return new Option<T>(previous);
		}
	}
}
