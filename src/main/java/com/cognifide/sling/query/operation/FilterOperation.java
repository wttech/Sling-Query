package com.cognifide.sling.query.operation;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.EmptyIterator;
import com.cognifide.sling.query.Operation;
import com.cognifide.sling.query.ResourcePredicate;
import com.cognifide.sling.query.iterator.ArrayIterator;

public class FilterOperation implements Operation {

	private final ResourcePredicate predicate;

	public FilterOperation(ResourcePredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public Iterator<Resource> getResources(Resource resource) {
		if (predicate.accepts(resource)) {
			return new ArrayIterator(resource);
		} else {
			return EmptyIterator.INSTANCE;
		}
	}

}
