package com.cognifide.sling.query.predicate;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.ArrayIterator;

public class FunctionPredicate implements ResourcePredicate {

	private final IteratorToIteratorFunction function;

	public FunctionPredicate(IteratorToIteratorFunction function) {
		this.function = function;
	}

	@Override
	public boolean accepts(Resource resource) {
		return function.apply(new ArrayIterator(resource)).hasNext();
	}

}
