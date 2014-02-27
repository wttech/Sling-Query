package com.cognifide.sling.query.resource.jcr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cognifide.sling.query.selector.parser.Attribute;
import com.cognifide.sling.query.selector.parser.Selector;
import com.cognifide.sling.query.selector.parser.SelectorParser;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public final class JcrSelectorParser {

	private JcrSelectorParser() {
	}

	public static String parse(String selector, String rootPath) {
		List<Selector> selectorList = SelectorParser.parse(selector);
		List<SelectorSegment> segments = Collections.emptyList();
		if (!selectorList.isEmpty()) {
			segments = selectorList.get(0).getSegments();
		}
		if (segments.isEmpty()) {
			return prepareQuery(rootPath, null, null, Collections.<Attribute> emptyList());
		} else {
			SelectorSegment s = segments.get(0);
			return prepareQuery(rootPath, s.getType(), s.getName(), s.getAttributes());
		}
	}

	private static String prepareQuery(String rootPath, String resourceType, String resourceName,
			List<Attribute> attributes) {
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
			List<Attribute> attributes) {
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
			for (Attribute a : attributes) {
				String attributeCondition = getAttributeCondition(a);
				if (StringUtils.isNotBlank(attributeCondition)) {
					conditions.add(attributeCondition);
				}
			}
		}
		return conditions;
	}

	private static String getAttributeCondition(Attribute attribute) {
		JcrOperator operator = JcrOperator.getSelectorOperator(attribute.getOperator());
		String value = StringUtils.replace(attribute.getValue(), "'", "''");
		return operator.getJcrQueryFragment(attribute.getKey(), value);
	}
}
