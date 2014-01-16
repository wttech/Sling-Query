package com.cognifide.sling.query.operation;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.Operation;
import com.cognifide.sling.query.ResourcePredicate;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;

public class ChildrenOperation implements Operation {

	private final ResourcePredicate predicate;

	public ChildrenOperation(ResourcePredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public Iterator<Resource> getResources(Resource resource) {
		Iterator<Resource> children = resource.listChildren();
		return new FilteringIteratorWrapper(children, predicate);
	}

}
