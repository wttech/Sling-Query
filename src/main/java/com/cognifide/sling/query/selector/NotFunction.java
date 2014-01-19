package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;
import com.cognifide.sling.query.predicate.RejectingPredicate;

public class NotFunction implements IteratorToIteratorFunction {

	private final Selector selector;

	public NotFunction(Selector selector) {
		this.selector = selector;
	}

	@Override
	public Iterator<Resource> apply(Iterator<Resource> iterator) {
		if (selector.getFunctions().isEmpty()) {
			return new FilteringIteratorWrapper(iterator, new RejectingPredicate(selector.getPredicate()));
		} else {
			List<Resource> list = new ArrayList<Resource>();
			while (iterator.hasNext()) {
				list.add(iterator.next());
			}
			Iterator<Resource> rejected = selector.applySelectorFunctions(list.iterator());
			List<String> rejectedPaths = new ArrayList<String>();
			while (rejected.hasNext()) {
				rejectedPaths.add(rejected.next().getPath());
			}
			Iterator<Resource> listIterator = list.iterator();
			while (listIterator.hasNext()) {
				if (rejectedPaths.contains(listIterator.next().getPath())) {
					listIterator.remove();
				}
			}
			return list.iterator();
		}
	}
}
