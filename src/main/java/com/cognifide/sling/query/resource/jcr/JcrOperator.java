package com.cognifide.sling.query.resource.jcr;

public enum JcrOperator {
	CONTAINS("*=") {
		@Override
		public String getJcrQueryFragment(String key, String value) {
			return String.format("s.[%s] LIKE '%%%s%%'", key, value);
		}
	},
	CONTAINS_WORD("~=") {
		@Override
		public String getJcrQueryFragment(String key, String value) {
			return CONTAINS.getJcrQueryFragment(key, value);
		}
	},
	ENDS_WITH("$=") {
		@Override
		public String getJcrQueryFragment(String key, String value) {
			return String.format("s.[%s] LIKE '%%%s'", key, value);
		}
	},
	EQUALS("=") {
		@Override
		public String getJcrQueryFragment(String key, String value) {
			return String.format("s.[%s] = '%s'", key, value);
		}
	},
	NOT_EQUAL("!=") {
		@Override
		public String getJcrQueryFragment(String key, String value) {
			return String.format("s.[%s] != '%s'", key, value);
		}
	},
	STARTS_WITH("^=") {
		@Override
		public String getJcrQueryFragment(String key, String value) {
			return String.format("s.[%s] LIKE '%s%%'", key, value);
		}
	};

	private final String operator;

	private JcrOperator(String operator) {
		this.operator = operator;
	}

	public abstract String getJcrQueryFragment(String key, String value);

	public static JcrOperator getSelectorOperator(String operator) {
		for (JcrOperator o : values()) {
			if (o.operator.equals(operator)) {
				return o;
			}
		}
		return EQUALS;
	}
}
