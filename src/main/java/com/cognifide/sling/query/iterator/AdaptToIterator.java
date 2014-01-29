package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.sling.api.resource.Resource;

public class AdaptToIterator<T> implements Iterator<T> {

	private final Iterator<Resource> iterator;

	private final Class<? extends T> clazz;

	private T currentModel;

	public AdaptToIterator(Iterator<Resource> iterator, Class<? extends T> clazz) {
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
			currentModel = iterator.next().adaptTo(clazz);
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
