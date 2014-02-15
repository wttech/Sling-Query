package com.cognifide.sling.query.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.ArrayIterator;
import com.cognifide.sling.query.iterator.EmptyIterator;

public class PrevFunction implements ResourceToIteratorFunction {

	private final Predicate<Resource> until;

	public PrevFunction(Predicate<Resource> until) {
		this.until = until;
	}

	@Override
	public Iterator<Resource> apply(Resource resource) {
		List<Resource> prevSiblings = getSiblingsBeforeMe(resource);
		prevSiblings = subListAfterLast(prevSiblings, until);
		if (prevSiblings.isEmpty()) {
			return EmptyIterator.INSTANCE;
		}
		if (until == null) {
			Resource prev = prevSiblings.get(prevSiblings.size() - 1);
			return new ArrayIterator<Resource>(prev);
		} else {
			return prevSiblings.iterator();
		}
	}

	private static List<Resource> getSiblingsBeforeMe(Resource resource) {
		Resource parent = resource.getParent();
		if (parent == null) {
			return Collections.emptyList();
		}

		String resourceName = resource.getName();
		List<Resource> prevResources = new ArrayList<Resource>();
		Iterator<Resource> siblings = parent.listChildren();
		while (siblings.hasNext()) {
			Resource sibling = siblings.next();
			if (sibling.getName().equals(resourceName)) {
				break;
			}
			prevResources.add(sibling);
		}
		return prevResources;
	}

	private static List<Resource> subListAfterLast(List<Resource> resources, Predicate<Resource> selector) {
		if (selector != null) {
			int i;
			for (i = resources.size() - 1; i >= 0; i--) {
				if (selector.accepts(resources.get(i))) {
					return resources.subList(i + 1, resources.size());
				}
			}
		}
		return resources;
	}
}
