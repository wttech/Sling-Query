package com.cognifide.sling.query.resource.jcr.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cognifide.sling.query.resource.jcr.JcrOperator;
import com.cognifide.sling.query.resource.jcr.JcrTypeResolver;
import com.cognifide.sling.query.resource.jcr.query.Formula.Operator;
import com.cognifide.sling.query.selector.parser.Attribute;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class JcrQueryBuilder {

	private final JcrTypeResolver typeResolver;

	public JcrQueryBuilder(JcrTypeResolver typeResolver) {
		this.typeResolver = typeResolver;
	}

	public String buildQuery(List<SelectorSegment> segments, String rootPath) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM [");
		query.append(findPrimaryType(segments));
		query.append("]");
		query.append(" AS s");

		String conditionString = getConditionString(segments, rootPath);
		if (StringUtils.isNotBlank(conditionString)) {
			query.append(" WHERE ").append(conditionString);
		}
		return query.toString();
	}

	private String getConditionString(List<SelectorSegment> segments, String rootPath) {
		Formula formula = prepareAlternativeConditions(segments);
		if (StringUtils.isNotBlank(rootPath) && !"/".equals(rootPath)) {
			List<Term> conditions = new ArrayList<Term>();
			conditions.add(new Atomic(String.format("ISDESCENDANTNODE([%s])", rootPath)));
			if (formula != null) {
				conditions.add(formula);
			}
			formula = new Formula(Operator.AND, conditions);
		}
		if (formula == null) {
			return null;
		} else {
			return formula.buildString();
		}
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

	private static Formula prepareAlternativeConditions(List<SelectorSegment> segments) {
		List<Term> list = new ArrayList<Term>();
		for (SelectorSegment segment : segments) {
			Formula conditions = prepareSegmentConditions(segment.getType(), segment.getName(),
					segment.getAttributes());
			if (conditions != null) {
				list.add(conditions);
			}
		}
		if (list.isEmpty()) {
			return null;
		} else {
			return new Formula(Operator.OR, list);
		}
	}

	private static Formula prepareSegmentConditions(String resourceType, String resourceName,
			List<Attribute> attributes) {
		List<Term> conditions = new ArrayList<Term>();
		if (StringUtils.isNotBlank(resourceType) && !StringUtils.contains(resourceType, ':')) {
			conditions.add(new Atomic(String.format("s.[sling:resourceType] = '%s'", resourceType)));
		}
		if (StringUtils.isNotBlank(resourceName)) {
			conditions.add(new Atomic(String.format("NAME(s) = '%s'", resourceName)));
		}
		if (attributes != null) {
			for (Attribute a : attributes) {
				String attributeCondition = getAttributeCondition(a);
				if (StringUtils.isNotBlank(attributeCondition)) {
					conditions.add(new Atomic(attributeCondition));
				}
			}
		}
		if (conditions.isEmpty()) {
			return null;
		} else {
			return new Formula(Operator.AND, conditions);
		}
	}

	private static String getAttributeCondition(Attribute attribute) {
		JcrOperator operator = JcrOperator.getSelectorOperator(attribute.getOperator());
		String value = StringUtils.replace(attribute.getValue(), "'", "''");
		return operator.getJcrQueryFragment(attribute.getKey(), value);
	}
}
