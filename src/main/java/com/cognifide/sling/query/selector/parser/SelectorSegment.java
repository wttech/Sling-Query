package com.cognifide.sling.query.selector.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.IteratorFactory;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;
import com.cognifide.sling.query.predicate.PropertyPredicate;
import com.cognifide.sling.query.selector.SelectorFilterPredicate;

public class SelectorSegment implements IteratorToIteratorFunction {
	private final String resourceType;

	private final String resourceName;

	private final List<PropertyPredicate> attributes;

	private final List<SelectorFunction> functions;

	private final HierarchyOperator hierarchyOperator;

	private final SearchStrategy strategy;

	public SelectorSegment(ParserContext context, boolean firstSegment, SearchStrategy strategy) {
		this.resourceType = context.getResourceType();
		this.resourceName = context.getResourceName();
		this.attributes = new ArrayList<PropertyPredicate>(context.getAttributes());
		this.functions = new ArrayList<SelectorFunction>(context.getFunctions());
		if (firstSegment) {
			hierarchyOperator = null;
		} else {
			hierarchyOperator = HierarchyOperator.findByCharacter(context.getHierarchyOperator());
		}
		this.strategy = strategy;
	}

	SelectorSegment(String resourceType, String resourceName, List<PropertyPredicate> attributes,
			List<SelectorFunction> functions, char hierarchyOperator, SearchStrategy strategy) {
		this.resourceType = resourceType;
		this.resourceName = resourceName;
		this.attributes = attributes;
		this.functions = functions;
		this.hierarchyOperator = HierarchyOperator.findByCharacter(hierarchyOperator);
		this.strategy = strategy;
	}

	@Override
	public Iterator<Resource> apply(Iterator<Resource> input) {
		Iterator<Resource> iterator = applyHierarchyOperator(input);
		iterator = new FilteringIteratorWrapper<Resource>(iterator, getFilter());
		for (SelectorFunction f : functions) {
			iterator = IteratorFactory.getIterator(f.function(strategy), iterator);
		}
		return iterator;
	}

	String getResourceType() {
		return resourceType;
	}

	String getResourceName() {
		return resourceName;
	}

	List<PropertyPredicate> getAttributes() {
		return attributes;
	}

	List<SelectorFunction> getFunctions() {
		return functions;
	}

	private Iterator<Resource> applyHierarchyOperator(Iterator<Resource> input) {
		if (hierarchyOperator == null) {
			return input;
		} else {
			return IteratorFactory.getIterator(hierarchyOperator.getFunction(strategy), input);
		}
	}

	private Predicate<Resource> getFilter() {
		return new SelectorFilterPredicate(resourceType, resourceName, attributes);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		SelectorSegment rhs = (SelectorSegment) obj;
		return new EqualsBuilder().append(resourceType, rhs.resourceType).append(attributes, rhs.attributes)
				.append(functions, rhs.functions).append(hierarchyOperator, rhs.hierarchyOperator).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(resourceType).append(attributes).append(functions)
				.append(hierarchyOperator).toHashCode();
	}

	@Override
	public String toString() {
		return String.format("SelectorSegment[%s,%s,%s,%s]", resourceType, attributes, functions,
				hierarchyOperator);
	}
}
