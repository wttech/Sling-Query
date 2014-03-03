package com.cognifide.sling.query.resource.jcr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cognifide.sling.query.selector.parser.Attribute;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class JcrQueryBuilder {

	private final JcrTypeResolver typeResolver;

	public JcrQueryBuilder(JcrTypeResolver typeResolver) {
		this.typeResolver = typeResolver;
	}

	public String buildQuery(List<SelectorSegment> segments, String rootPath) {
		String path = null;
		if (StringUtils.isNotBlank(rootPath) && !"/".equals(rootPath)) {
			path = rootPath;
		}
		List<List<String>> allConditions = prepareAllConditions(segments);

		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM [");
		query.append(findPrimaryType(segments));
		query.append("]");
		query.append(" AS s");
		if (allConditions.isEmpty() && path == null) {
			return query.toString();
		}
		query.append(" WHERE");
		if (path != null) {
			query.append(" ISDESCENDANTNODE([").append(rootPath).append("])");
			if (!allConditions.isEmpty()) {
				query.append(" AND");
			}
		}
		if (allConditions.size() > 1 && path != null) {
			query.append(" (");
		}
		for (int i = 0; i < allConditions.size(); i++) {
			List<String> conditions = allConditions.get(i);
			if (i > 0) {
				query.append(" OR");
			}
			if (conditions.size() > 1 && allConditions.size() > 1) {
				query.append(" (");
			} else {
				query.append(" ");
			}
			query.append(conditions.remove(0));
			for (String condition : conditions) {
				query.append(" AND ").append(condition);
			}
			if (conditions.size() > 0 && allConditions.size() > 1) {
				query.append(")");
			}

		}
		if (allConditions.size() > 1 && path != null) {
			query.append(")");
		}
		return query.toString().replace("( ", "(");
	}

	private String findPrimaryType(List<SelectorSegment> segments) {
		String result = null;
		for (SelectorSegment s : segments) {
			String type = s.getType();
			if (!typeResolver.isJcrType(type)) {
				continue;
			}
			if (result == null) {
				result = type;
			} else if (typeResolver.isSubtype(type, result)) {
				result = type;
			} else if (!typeResolver.isSubtype(result, type)) {
				result = "nt:base";
			}
		}
		if (result == null) {
			result = "nt:base";
		}
		return result;
	}

	private static List<List<String>> prepareAllConditions(List<SelectorSegment> segments) {
		List<List<String>> list = new ArrayList<List<String>>();
		for (SelectorSegment segment : segments) {
			List<String> conditions = prepareConditions(segment.getType(), segment.getName(),
					segment.getAttributes());
			if (!conditions.isEmpty()) {
				list.add(conditions);
			}
		}
		return list;
	}

	private static List<String> prepareConditions(String resourceType, String resourceName,
			List<Attribute> attributes) {
		List<String> conditions = new ArrayList<String>();
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
