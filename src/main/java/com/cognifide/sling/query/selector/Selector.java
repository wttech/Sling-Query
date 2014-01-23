package com.cognifide.sling.query.selector;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.predicate.FunctionPredicate;
import com.cognifide.sling.query.selector.parser.SelectorParser;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class Selector implements IteratorToIteratorFunction {

	private final List<SelectorSegment> segments;

	public Selector(String selectorString) {
		if (StringUtils.isBlank(selectorString)) {
			segments = Collections.emptyList();
		} else {
			segments = SelectorParser.parse(selectorString).getSegments();
		}
	}

	@Override
	public Iterator<Resource> apply(Iterator<Resource> input) {
		Iterator<Resource> iterator = input;
		for (SelectorSegment segment : segments) {
			iterator = segment.apply(iterator);
		}
		return iterator;
	}

	public ResourcePredicate asPredicate() {
		return new FunctionPredicate(this);
	}
}
