package com.cognifide.sling.query.iterator;

import java.util.ArrayList;
import java.util.Iterator;

import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.Option;

public class DescendantsIterator<T> extends AbstractIterator<Option<T>> {

	private final Iterator<Option<T>> input;

	private Option<T> current;

	private Iterator<T> descendants;

	private final TreeProvider<T> provider;

	public DescendantsIterator(Iterator<Option<T>> input, Iterator<T> descendants, TreeProvider<T> provider) {
		this.input = input;
		this.current = null;
		this.descendants = new ArrayList<T>(new LazyList<T>(descendants)).iterator();
		this.provider = provider;
	}

	@Override
	protected Option<T> getElement() {
		if (current == null) {
			if (input.hasNext()) {
				current = input.next();
			} else {
				return null;
			}
		}
		return getDescendant();
	}

	private Option<T> getDescendant() {
		while (descendants.hasNext()) {
			T descendantCandidate = descendants.next();
			if (provider.isDescendant(current.getElement(), descendantCandidate)) {
				descendants.remove();
				return Option.of(descendantCandidate, current.getArgumentId());
			}
		}
		Option<T> result = Option.empty(current.getArgumentId());
		current = null;
		return result;
	}
}
