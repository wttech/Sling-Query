package com.cognifide.sling.query.operation;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.EmptyIterator;
import com.cognifide.sling.query.Operation;
import com.cognifide.sling.query.ResourcePredicate;

public class ClosestOperation implements Operation {

	private final ResourcePredicate predicate;

	public ClosestOperation(ResourcePredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public Iterator<Resource> getResources(Resource resource) {
		Resource current = resource;
		do {
			current = current.getParent();
			if (current == null) {
				return EmptyIterator.INSTANCE;
			}
		} while (!predicate.accepts(current));
		return Arrays.asList(current).iterator();
	}

}
