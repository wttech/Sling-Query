package com.cognifide.sling.query.function;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.selector.Selector;

public class NotFunction implements IteratorToIteratorFunction {

	private final Selector selector;

	public NotFunction(Selector selector) {
		this.selector = selector;
	}

	@Override
	public Iterator<Resource> apply(Iterator<Resource> input) {
		List<Resource> list = iteratorToList(input);
		List<Resource> matching = iteratorToList(selector.apply(list.iterator()));

		Iterator<Resource> iterator = list.iterator();
		while (iterator.hasNext()) {
			if (matching.contains(iterator.next())) {
				iterator.remove();
			}
		}
		return list.iterator();
	}

	private static List<Resource> iteratorToList(Iterator<Resource> iterator) {
		List<Resource> list = new ArrayList<Resource>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}
}
