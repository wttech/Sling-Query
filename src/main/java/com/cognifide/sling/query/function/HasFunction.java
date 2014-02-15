package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;
import com.cognifide.sling.query.selector.Selector;

public class HasFunction implements ResourceToResourceFunction {

	private FindFunction findFunction;

	private Predicate<Resource> predicate;

	public HasFunction(String selectorString, SearchStrategy searchStrategy) {
		this.findFunction = new FindFunction(selectorString, searchStrategy);
		this.predicate = new Selector(selectorString, searchStrategy).asPredicate();
	}

	@Override
	public Resource apply(Resource input) {
		Iterator<Resource> iterator = findFunction.apply(input);
		iterator = new FilteringIteratorWrapper<Resource>(iterator, predicate);
		if (iterator.hasNext()) {
			return input;
		} else {
			return null;
		}
	}
}