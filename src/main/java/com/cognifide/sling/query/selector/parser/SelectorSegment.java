package com.cognifide.sling.query.selector.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SelectorSegment {
	private final String type;

	private final String name;

	private final List<Attribute> attributes;

	private final List<Modifier> modifiers;

	private final char hierarchyOperator;

	public SelectorSegment(ParserContext context, boolean firstSegment) {
		this.type = context.getType();
		this.name = context.getName();
		this.attributes = new ArrayList<Attribute>(context.getAttributes());
		this.modifiers = new ArrayList<Modifier>(context.getModifiers());
		if (firstSegment) {
			hierarchyOperator = 0;
		} else {
			hierarchyOperator = context.getHierarchyOperator();
		}
	}

	SelectorSegment(String type, String name, List<Attribute> attributes,
			List<Modifier> modifiers, char hierarchyOperator) {
		this.type = type;
		this.name = name;
		this.attributes = attributes;
		this.modifiers = modifiers;
		this.hierarchyOperator = hierarchyOperator;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public char getHierarchyOperator() {
		return hierarchyOperator;
	}

	public List<Modifier> getModifiers() {
		return modifiers;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		SelectorSegment rhs = (SelectorSegment) obj;
		return new EqualsBuilder().append(type, rhs.type).append(attributes, rhs.attributes)
				.append(modifiers, rhs.modifiers).append(hierarchyOperator, rhs.hierarchyOperator).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(type).append(attributes).append(modifiers)
				.append(hierarchyOperator).toHashCode();
	}

	@Override
	public String toString() {
		return String.format("SelectorSegment[%s,%s,%s,%s]", type, attributes, modifiers,
				hierarchyOperator);
	}
}
