package com.cognifide.sling.query;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;
import com.cognifide.sling.query.predicate.FilterPredicate;

public class Operation {
	private final Function<?, ?> function;

	private final ResourcePredicate predicate;

	public Operation(Function<?, ?> function, String filter) {
		this.function = function;
		if (StringUtils.isBlank(filter)) {
			this.predicate = null;
		} else {
			this.predicate = new FilterPredicate(filter);
		}
	}

	public Operation(Function<?, ?> function, ResourcePredicate predicate) {
		this.function = function;
		this.predicate = predicate;
	}

	public Iterator<Resource> getIterator(Iterator<Resource> iterator) {
		Iterator<Resource> newIterator = IteratorFactory.getIterator(function, iterator);
		if (predicate != null) {
			newIterator = new FilteringIteratorWrapper(newIterator, predicate);
		}
		return newIterator;
	}
}
