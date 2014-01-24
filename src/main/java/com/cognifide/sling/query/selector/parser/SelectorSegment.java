package com.cognifide.sling.query.selector.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.IteratorFactory;
import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;
import com.cognifide.sling.query.predicate.PropertyPredicate;
import com.cognifide.sling.query.selector.SelectorFilterPredicate;

public class SelectorSegment implements IteratorToIteratorFunction {
	private final String resourceType;

	private final List<PropertyPredicate> attributes;

	private final List<SelectorFunction> functions;

	private final Function<?, ?> hierarchyFunction;

	public SelectorSegment(ParserContext context, boolean firstSegment) {
		this.resourceType = context.getResourceType();
		this.attributes = new ArrayList<PropertyPredicate>(context.getAttributes());
		this.functions = new ArrayList<SelectorFunction>(context.getFunctions());
		if (firstSegment) {
			hierarchyFunction = null;
		} else {
			char hierarchyOperator = context.getHierarchyOperator();
			hierarchyFunction = HierarchyOperator.findByCharacter(hierarchyOperator).getFunction();
		}
	}

	@Override
	public Iterator<Resource> apply(Iterator<Resource> input) {
		Iterator<Resource> iterator = applyHierarchyOperator(input);
		iterator = new FilteringIteratorWrapper(iterator, getFilter());
		for (SelectorFunction f : functions) {
			iterator = IteratorFactory.getIterator(f.function(), iterator);
		}
		return iterator;
	}

	String getResourceType() {
		return resourceType;
	}

	List<PropertyPredicate> getAttributes() {
		return attributes;
	}

	List<SelectorFunction> getFunctions() {
		return functions;
	}

	private Iterator<Resource> applyHierarchyOperator(Iterator<Resource> input) {
		if (hierarchyFunction == null) {
			return input;
		} else {
			return IteratorFactory.getIterator(hierarchyFunction, input);
		}
	}

	private ResourcePredicate getFilter() {
		return new SelectorFilterPredicate(resourceType, attributes);
	}

}
