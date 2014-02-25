package com.cognifide.sling.query.predicate;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public enum SelectorOperator {
	CONTAINS("*=") {
		@Override
		public boolean accepts(String property, String value) {
			return StringUtils.contains(property, value);
		}
	},
	CONTAINS_WORD("~=") {
		@Override
		public boolean accepts(String property, String value) {
			String quoted = Pattern.quote(value);
			String regex = String.format("(^| )%s( |$)", quoted);
			return property != null && Pattern.compile(regex).matcher(property).find();
		}
	},
	ENDS_WITH("$=") {
		@Override
		public boolean accepts(String property, String value) {
			return StringUtils.endsWith(property, value);
		}
	},
	EQUALS("=") {
		@Override
		public boolean accepts(String property, String value) {
			return StringUtils.equals(property, value);
		}
	},
	NOT_EQUAL("!=") {
		@Override
		public boolean accepts(String property, String value) {
			return !StringUtils.equals(property, value);
		}
	},
	STARTS_WITH("^=") {
		@Override
		public boolean accepts(String property, String value) {
			return StringUtils.startsWith(property, value);
		}
	};

	private final String operator;

	private SelectorOperator(String operator) {
		this.operator = operator;
	}

	public abstract boolean accepts(String key, String value);

	public static SelectorOperator getSelectorOperator(String operator) {
		for (SelectorOperator o : values()) {
			if (o.operator.equals(operator)) {
				return o;
			}
		}
		return EQUALS;
	}
}
