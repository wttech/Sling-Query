package com.cognifide.sling.query.iterator;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.apache.sling.api.resource.Resource;

public enum EmptyIterator implements ListIterator<Resource> {

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

	@Override
	public boolean hasPrevious() {
		return false;
	}

	@Override
	public Resource previous() {
		throw new NoSuchElementException();
	}

	@Override
	public int nextIndex() {
		return 0;
	}

	@Override
	public int previousIndex() {
		return 0;
	}

	@Override
	public void set(Resource e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(Resource e) {
		throw new UnsupportedOperationException();
	}

}
