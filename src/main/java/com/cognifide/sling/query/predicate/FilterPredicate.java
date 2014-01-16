package com.cognifide.sling.query.predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.ResourcePredicate;

public class FilterPredicate implements ResourcePredicate {

	private String resourceType;

	private List<PropertyPredicate> properties = new ArrayList<PropertyPredicate>();

	public FilterPredicate(String filter) {
		if (StringUtils.isNotBlank(filter)) {
			parseFilter(filter);
		}
	}

	private void parseFilter(String filter) {
		Pattern pattern = Pattern.compile("^[^\\[]]+");
		Matcher matcher = pattern.matcher(filter);
		if (matcher.find()) {
			resourceType = matcher.group();
		}
		pattern = Pattern.compile("^\\[([\\]]+)\\]");
		matcher = pattern.matcher(filter);
		if (matcher.find(resourceType.length())) {
			do {
				String[] split = matcher.group().split("=");
				if (split.length == 2) {
					properties.add(new PropertyPredicate(split[0], split[1]));
				}
			} while (matcher.find());
		}
	}

	@Override
	public boolean accepts(Resource resource) {
		if (StringUtils.isNotBlank(resourceType) && !resource.isResourceType(resourceType)) {
			return false;
		}
		for (PropertyPredicate predicate : properties) {
			if (!predicate.accepts(resource)) {
				return false;
			}
		}
		return true;
	}
}
