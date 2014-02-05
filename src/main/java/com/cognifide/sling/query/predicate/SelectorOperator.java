package com.cognifide.sling.query.predicate;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public enum SelectorOperator {
	CONTAINS("*=") {
		@Override
		public boolean accepts(String property, String value) {
			return StringUtils.contains(property, value);
		}

		@Override
		public String getJcrQueryFragment(String key, String value) {
			return String.format("s.[%s] LIKE '%%%s%%'", key, value);
		}
	},
	CONTAINS_WORD("~=") {
		@Override
		public boolean accepts(String property, String value) {
			String quoted = Pattern.quote(value);
			String regex = String.format("(^| )%s( |$)", quoted);
			return property != null && Pattern.compile(regex).matcher(property).find();
		}

		@Override
		public String getJcrQueryFragment(String key, String value) {
			return CONTAINS.getJcrQueryFragment(key, value);
		}
	},
	ENDS_WITH("$=") {
		@Override
		public boolean accepts(String property, String value) {
			return StringUtils.endsWith(property, value);
		}

		@Override
		public String getJcrQueryFragment(String key, String value) {
			return String.format("s.[%s] LIKE '%%%s'", key, value);
		}
	},
	EQUALS("=") {
		@Override
		public boolean accepts(String property, String value) {
			return StringUtils.equals(property, value);
		}

		@Override
		public String getJcrQueryFragment(String key, String value) {
			return String.format("s.[%s] = '%s'", key, value);
		}
	},
	NOT_EQUAL("!=") {
		@Override
		public boolean accepts(String property, String value) {
			return !StringUtils.equals(property, value);
		}

		@Override
		public String getJcrQueryFragment(String key, String value) {
			return String.format("s.[%s] != '%s'", key, value);
		}
	},
	STARTS_WITH("^=") {
		@Override
		public boolean accepts(String property, String value) {
			return StringUtils.startsWith(property, value);
		}

		@Override
		public String getJcrQueryFragment(String key, String value) {
			return String.format("s.[%s] LIKE '%s%%'", key, value);
		}
	};

	private final String operator;

	private SelectorOperator(String operator) {
		this.operator = operator;
	}

	public abstract boolean accepts(String key, String value);

	public abstract String getJcrQueryFragment(String key, String value);

	public static SelectorOperator getSelectorOperator(String operator) {
		for (SelectorOperator o : values()) {
			if (o.operator.equals(operator)) {
				return o;
			}
		}
		return EQUALS;
	}
}
