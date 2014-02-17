package com.cognifide.sling.query.function;

import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.ExcludingIterator;
import com.cognifide.sling.query.selector.Selector;

public class NotFunction implements IteratorToIteratorFunction {

	private final Selector selector;

	public NotFunction(Selector selector) {
		this.selector = selector;
	}

	@Override
	public Iterator<Resource> apply(Iterator<Resource> input) {
		List<Resource> list = new LazyList<Resource>(input);
		List<Resource> matching = new LazyList<Resource>(selector.apply(list.iterator()));
		return new ExcludingIterator<Resource>(list.iterator(), matching.iterator());
	}
}
