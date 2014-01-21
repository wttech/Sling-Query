package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.IteratorFactory;
import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.predicate.PropertyPredicate;
import com.cognifide.sling.query.selector.parser.ParsedSelectorFunction;
import com.cognifide.sling.query.selector.parser.ParserContext;
import com.cognifide.sling.query.selector.parser.SelectorParser;

public class Selector {

	private String resourceType;

	private List<PropertyPredicate> properties = new ArrayList<PropertyPredicate>();

	private List<ParsedSelectorFunction> functions = new ArrayList<ParsedSelectorFunction>();

	public Selector(String selectorString) {
		if (StringUtils.isNotBlank(selectorString)) {
			parseSelector(selectorString);
		}
	}

	private void parseSelector(String selectorString) {
		ParserContext context = SelectorParser.parse(selectorString);
		resourceType = context.getResourceType();
		for (String attribute : context.getAttributes()) {
			properties.add(new PropertyPredicate(attribute));
		}
		for (String function : context.getFunctions()) {
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
