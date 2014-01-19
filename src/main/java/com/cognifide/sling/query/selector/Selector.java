package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.IteratorFactory;
import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.predicate.PropertyPredicate;

public class Selector {

	private String resourceType;

	private List<PropertyPredicate> properties = new ArrayList<PropertyPredicate>();

	private List<ParsedSelectorFunction> functions = new ArrayList<ParsedSelectorFunction>();

	public Selector(String selectorString) {
		if (StringUtils.isNotBlank(selectorString)) {
			parseFilter(selectorString);
		}
	}

	private void parseFilter(String selectorString) {
		SelectorParser parser = new SelectorParser();
		parser.parse(selectorString);
		resourceType = parser.getResourceType();
		for (String attribute : parser.getAttributes()) {
			properties.add(new PropertyPredicate(attribute));
		}
		for (String function : parser.getFunctions()) {
			functions.add(new ParsedSelectorFunction(function));
		}
	}

	public ResourcePredicate getPredicate() {
		return new SelectorFilterPredicate(resourceType, properties);
	}

	public Iterator<Resource> applySelectorFunctions(Iterator<Resource> iterator) {
		Iterator<Resource> wrappedIterator = iterator;
		for (ParsedSelectorFunction function : functions) {
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

	List<ParsedSelectorFunction> getFunctions() {
		return functions;
	}

}
