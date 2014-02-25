package com.cognifide.sling.query.selector.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.cognifide.sling.query.IteratorFactory;
import com.cognifide.sling.query.TreeStructureProvider;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.FilteringIteratorWrapper;
import com.cognifide.sling.query.predicate.PropertyPredicate;

public class SelectorSegment<T> implements IteratorToIteratorFunction<T> {
	private final String resourceType;

	private final String resourceName;

	private final List<PropertyPredicate> attributes;

	private final List<SelectorFunction> functions;

	private final HierarchyOperator hierarchyOperator;

	private final SearchStrategy strategy;

	private final TreeStructureProvider<T> provider;

	public SelectorSegment(ParserContext<T> context, boolean firstSegment, SearchStrategy strategy,
			TreeStructureProvider<T> provider) {
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
		this.provider = provider;
	}

	SelectorSegment(String resourceType, String resourceName, List<PropertyPredicate> attributes,
			List<SelectorFunction> functions, char hierarchyOperator, SearchStrategy strategy,
			TreeStructureProvider<T> provider) {
		this.resourceType = resourceType;
		this.resourceName = resourceName;
		this.attributes = attributes;
		this.functions = functions;
		this.hierarchyOperator = HierarchyOperator.findByCharacter(hierarchyOperator);
		this.strategy = strategy;
		this.provider = provider;
	}

	@Override
	public Iterator<T> apply(Iterator<T> input) {
		Iterator<T> iterator = applyHierarchyOperator(input);
		iterator = new FilteringIteratorWrapper<T>(iterator, getFilter());
		for (SelectorFunction f : functions) {
			iterator = IteratorFactory.getIterator(f.function(strategy, provider), iterator);
		}
		return iterator;
	}

	public String getResourceType() {
		return resourceType;
	}

	public String getResourceName() {
		return resourceName;
	}

	public List<PropertyPredicate> getAttributes() {
		return attributes;
	}

	List<SelectorFunction> getFunctions() {
		return functions;
	}

	private Iterator<T> applyHierarchyOperator(Iterator<T> input) {
		if (hierarchyOperator == null) {
			return input;
		} else {
			return IteratorFactory.getIterator(hierarchyOperator.getFunction(strategy, provider), input);
		}
	}

	private Predicate<T> getFilter() {
		return provider.getPredicate(resourceType, resourceName, attributes);
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
		@SuppressWarnings("unchecked")
		SelectorSegment<T> rhs = (SelectorSegment<T>) obj;
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
