package com.cognifide.sling.query.iterator;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.TreeProvider;

public class ParentsIterator<T> extends AbstractIterator<T> {

	private final Predicate<T> until;

	private final TreeProvider<T> provider;
	
	private T currentResource;

	public ParentsIterator(Predicate<T> until, T currentResource, TreeProvider<T> provider) {
		this.currentResource = currentResource;
		this.until = until;
		this.provider = provider;
	}

	@Override
	protected T getElement() {
		if (currentResource == null) {
			return null;
		}
		currentResource = provider.getParent(currentResource);

		if (currentResource == null) {
			return null;
		}

		if (until != null && until.accepts(currentResource)) {
			return null;
		}

		return currentResource;
	}

}
