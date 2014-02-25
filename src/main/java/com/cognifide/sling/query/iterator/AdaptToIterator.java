package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.sling.api.adapter.Adaptable;

public class AdaptToIterator<F, T> implements Iterator<T> {

	private final Iterator<F> iterator;

	private final Class<? extends T> clazz;

	private T currentModel;

	public AdaptToIterator(Iterator<F> iterator, Class<? extends T> clazz) {
		this.clazz = clazz;
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		if (currentModel == null) {
			getCurrentModel();
		}
		return currentModel != null;
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		T model = currentModel;
		currentModel = null;
		return model;
	}

	public void getCurrentModel() {
		while (iterator.hasNext()) {
			F element = iterator.next();
			if (element instanceof Adaptable) {
				currentModel = ((Adaptable) element).adaptTo(clazz);
			}
			if (currentModel != null) {
				break;
			}
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
