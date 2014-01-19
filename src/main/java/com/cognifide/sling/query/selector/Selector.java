package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.IteratorFactory;
import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.predicate.PropertyPredicate;

public class Selector {

	private static final Pattern RESOURCE_TYPE = Pattern.compile("^[^:\\[][^\\[]*");

	private static final Pattern ATTRIBUTE = Pattern.compile("\\[([^\\]]+)\\]");

	private static final Pattern FUNCTION = Pattern.compile(":([a-z]+)(\\(([^)]+)\\))?");

	private String resourceType;

	private List<PropertyPredicate> properties = new ArrayList<PropertyPredicate>();

	private List<ParsedSelectorFunction> functions = new ArrayList<ParsedSelectorFunction>();

	public Selector(String selectorString) {
		if (StringUtils.isNotBlank(selectorString)) {
			parseFilter(selectorString);
		}
	}

	private void parseFilter(String selectorString) {
		int offset = 0;

		Matcher m = RESOURCE_TYPE.matcher(selectorString);
		if (m.find()) {
			offset = m.end();
			resourceType = m.group();
		}
		m = ATTRIBUTE.matcher(selectorString);
		if (m.find(offset)) {
			do {
				offset = m.end();
				String[] split = StringUtils.split(m.group(1), '=');
				if (split.length == 2) {
					properties.add(new PropertyPredicate(split[0], split[1]));
				}
			} while (m.find());
		}
		m = FUNCTION.matcher(selectorString);
		if (m.find(offset)) {
			do {
				offset = m.end();
				String functionId = m.group(1);
				String argument = m.group(3);
				functions.add(new ParsedSelectorFunction(functionId, argument));
			} while (m.find());
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

	Object getFunctions() {
		return functions;
	}

}
