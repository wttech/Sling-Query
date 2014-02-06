package com.cognifide.sling.query.selector.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.predicate.PropertyPredicate;

public final class JcrSelectorParser {

	private JcrSelectorParser() {
	}

	public static String parse(String selector, String rootPath) {
		ParserContext context = SelectorParser.parse(selector, SearchStrategy.DFS); // search strategy is not
																					// used here
		if (context.getSegments().isEmpty()) {
			return prepareQuery(rootPath, null, null, Collections.<PropertyPredicate> emptyList());
		} else {
			SelectorSegment s = context.getSegments().get(0);
			return prepareQuery(rootPath, s.getResourceType(), s.getResourceName(), s.getAttributes());
		}
	}

	private static String prepareQuery(String rootPath, String resourceType, String resourceName,
			List<PropertyPredicate> attributes) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM");
		if (StringUtils.contains(resourceType, ':')) {
			query.append(" [").append(resourceType).append(']');
		} else {
			query.append(" [nt:base]");
		}
		query.append(" AS s");
		List<String> conditions = prepareConditions(rootPath, resourceType, resourceName, attributes);
		if (!conditions.isEmpty()) {
			query.append(" WHERE ").append(conditions.remove(0));
			for (String condition : conditions) {
				query.append(" AND ").append(condition);
			}
		}
		return query.toString();
	}

	private static List<String> prepareConditions(String rootPath, String resourceType, String resourceName,
			List<PropertyPredicate> attributes) {
		List<String> conditions = new ArrayList<String>();
		if (StringUtils.isNotBlank(rootPath) && !"/".equals(rootPath)) {
			conditions.add(String.format("ISDESCENDANTNODE([%s])", rootPath));
		}
		if (StringUtils.isNotBlank(resourceType) && !StringUtils.contains(resourceType, ':')) {
			conditions.add(String.format("s.[sling:resourceType] = '%s'", resourceType));
		}
		if (StringUtils.isNotBlank(resourceName)) {
			conditions.add(String.format("NAME(s) = '%s'", resourceName));
		}
		if (attributes != null) {
			for (PropertyPredicate a : attributes) {
				String attributeCondition = a.toJcrString();
				if (StringUtils.isNotBlank(attributeCondition)) {
					conditions.add(attributeCondition);
				}
			}
		}
		return conditions;
	}
}
