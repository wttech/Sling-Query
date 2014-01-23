package com.cognifide.sling.query.selector.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.IteratorFactory;
import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;
import com.cognifide.sling.query.predicate.PropertyPredicate;
import com.cognifide.sling.query.selector.SelectorFilterPredicate;

public class SelectorSegment implements IteratorToIteratorFunction {
	private final String resourceType;

	private final List<PropertyPredicate> attributes;

	private final List<SelectorFunction> functions;

	private final char hierarchyOperator;

	public SelectorSegment(ParserContext context) {
		this.resourceType = context.getResourceType();
		this.attributes = new ArrayList<PropertyPredicate>(context.getAttributes());
		this.functions = new ArrayList<SelectorFunction>(context.getFunctions());
		this.hierarchyOperator = context.getHierarchyOperator();
	}

	public String getResourceType() {
		return resourceType;
	}

	public List<PropertyPredicate> getAttributes() {
		return attributes;
	}

	public List<SelectorFunction> getFunctions() {
		return functions;
	}

	public char getHierarchyOperator() {
		return hierarchyOperator;
	}

	@Override
	public Iterator<Resource> apply(Iterator<Resource> input) {
		ResourcePredicate filter = new SelectorFilterPredicate(resourceType, attributes);
		Iterator<Resource> iterator = new FilteringIteratorWrapper(input, filter);
		for (SelectorFunction f : functions) {
			iterator = IteratorFactory.getIterator(f.function(), iterator);
		}
		return iterator;
	}
}
