package com.cognifide.sling.query;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;
import com.cognifide.sling.query.selector.Selector;

public class Operation {
	private final Function<?, ?> function;

	private final Selector selector;

	private final ResourcePredicate predicate;

	public Operation(Function<?, ?> function, String selector) {
		this.function = function;
		if (StringUtils.isBlank(selector)) {
			this.selector = null;
			this.predicate = null;
		} else {
			this.selector = new Selector(selector);
			this.predicate = this.selector.getPredicate();
		}
	}

	public Operation(Function<?, ?> function, ResourcePredicate predicate) {
		this.function = function;
		this.predicate = predicate;
		this.selector = null;
	}

	public Iterator<Resource> getIterator(Iterator<Resource> iterator) {
		Iterator<Resource> newIterator = IteratorFactory.getIterator(function, iterator);
		if (predicate != null) {
			newIterator = new FilteringIteratorWrapper(newIterator, predicate);
		}
		if (selector != null) {
			newIterator = selector.wrapWithFilters(newIterator);
		}
		return newIterator;
	}
}
