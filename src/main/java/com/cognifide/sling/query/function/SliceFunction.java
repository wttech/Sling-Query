package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.SliceIterator;

public class SliceFunction implements IteratorToIteratorFunction {

	private final int from;

	private final Integer to;

	public SliceFunction(int from, int to) {
		this.from = from;
		this.to = to;
	}

	public SliceFunction(int from) {
		this.from = from;
		this.to = null;
	}

	@Override
	public Iterator<Resource> apply(Iterator<Resource> resources) {
		if (to == null) {
			return new SliceIterator<Resource>(resources, from);
		} else {
			return new SliceIterator<Resource>(resources, from, to);
		}
	}
}