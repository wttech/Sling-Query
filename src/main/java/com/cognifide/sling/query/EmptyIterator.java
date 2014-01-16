package com.cognifide.sling.query;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.sling.api.resource.Resource;

public enum EmptyIterator implements Iterator<Resource> {

	INSTANCE;

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public Resource next() {
		throw new NoSuchElementException();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
