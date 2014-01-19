package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.iterator.ArrayIterator;

public class SiblingsFunction implements ResourceToIteratorFunction {

	@Override
	public Iterator<Resource> apply(Resource resource) {
		Resource parent = resource.getParent();
		if (parent == null) {
			return new ArrayIterator(resource);
		} else {
			return parent.listChildren();
		}
	}
}
