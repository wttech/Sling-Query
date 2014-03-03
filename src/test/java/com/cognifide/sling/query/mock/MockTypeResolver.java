package com.cognifide.sling.query.mock;

import java.util.Arrays;
import java.util.List;

import com.cognifide.sling.query.resource.jcr.JcrTypeResolver;

public class MockTypeResolver implements JcrTypeResolver {

	private static final List<String> TYPE_HIERARCHY = Arrays.asList("nt:base", "nt:unstructured", "cq:Page",
			"cq:Type");

	private static final List<String> OTHER_TYPES = Arrays.asList("jcr:otherType", "jcr:someType");

	@Override
	public boolean isJcrType(String name) {
		return TYPE_HIERARCHY.contains(name) || OTHER_TYPES.contains(name);
	}

	@Override
	public boolean isSubtype(String supertype, String subtype) {
		int i1 = TYPE_HIERARCHY.indexOf(supertype);
		int i2 = TYPE_HIERARCHY.indexOf(subtype);
		if (i1 == -1 || i2 == -1) {
			return false;
		}
		return i1 < i2;
	}

}
