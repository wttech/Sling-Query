package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.IteratorFactory;
import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.predicate.PropertyPredicate;
import com.cognifide.sling.query.selector.parser.SelectorFunction;
import com.cognifide.sling.query.selector.parser.ParserContext;
import com.cognifide.sling.query.selector.parser.SelectorParser;

public class Selector {

	private String resourceType;

	private List<PropertyPredicate> properties = new ArrayList<PropertyPredicate>();

	private List<SelectorFunction> functions = new ArrayList<SelectorFunction>();

	public Selector(String selectorString) {
		if (StringUtils.isNotBlank(selectorString)) {
			parseSelector(selectorString);
		}
	}

	private void parseSelector(String selectorString) {
		ParserContext context = SelectorParser.parse(selectorString);
		resourceType = context.getResourceType();
		properties.addAll(context.getAttributes());
		functions.addAll(context.getFunctions());
	}

	public ResourcePredicate getPredicate() {
		return new SelectorFilterPredicate(resourceType, properties);
	}

	public Iterator<Resource> applySelectorFunctions(Iterator<Resource> iterator) {
		Iterator<Resource> wrappedIterator = iterator;
		for (SelectorFunction function : functions) {
			wrappedIterator = IteratorFactory.getIterator(function.function(), wrappedIterator);
		}
		return wrappedIterator;
	}

	String getResourceType() {
		return resourceType;
	}

	List<PropertyPredicate> getProperties() {
		return properties;
	}

	List<SelectorFunction> getFunctions() {
		return functions;
	}

}
