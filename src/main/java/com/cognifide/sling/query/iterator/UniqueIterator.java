package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.Option;

public class UniqueIterator<T> extends AbstractIterator<Option<T>> {

	private final Iterator<Option<T>> iterator;

	private final TreeProvider<T> treeProvider;

	private T lastElement;

	public UniqueIterator(Iterator<Option<T>> input, TreeProvider<T> treeProvider) {
		this.iterator = input;
		this.treeProvider = treeProvider;
	}

	@Override
	protected Option<T> getElement() {
		if (!iterator.hasNext()) {
			return null;
		}
		Option<T> candidate = iterator.next();
		Option<T> result;
		if (treeProvider.sameElement(lastElement, candidate.getElement())) {
			result = Option.empty(candidate.getArgumentId());
		} else {
			result = candidate;
		}
		lastElement = candidate.getElement();
		return result;
	}

}
