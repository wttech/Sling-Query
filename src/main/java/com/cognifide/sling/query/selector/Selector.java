package com.cognifide.sling.query.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.cognifide.sling.query.api.ResourcePredicate;
import com.cognifide.sling.query.predicate.PropertyPredicate;

public class Selector {

	private String resourceType;

	private List<PropertyPredicate> properties = new ArrayList<PropertyPredicate>();

	public Selector(String selectorString) {
		if (StringUtils.isNotBlank(selectorString)) {
			parseFilter(selectorString);
		}
	}

	private void parseFilter(String selectorString) {
		Pattern pattern = Pattern.compile("^[^\\[]+");
		Matcher matcher = pattern.matcher(selectorString);
		if (matcher.find()) {
			resourceType = matcher.group();
		} else {
			resourceType = "";
		}
		pattern = Pattern.compile("\\[([^\\]]+)\\]");
		matcher = pattern.matcher(selectorString);
		if (matcher.find(resourceType.length())) {
			do {
				String[] split = matcher.group(1).split("=");
				if (split.length == 2) {
					properties.add(new PropertyPredicate(split[0], split[1]));
				}
			} while (matcher.find());
		}
	}

	public ResourcePredicate getPredicate() {
		return new SelectorFilterPredicate(resourceType, properties);
	}
}
