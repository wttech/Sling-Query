package com.cognifide.sling.query.resource.jcr;

public interface JcrTypeResolver {
	boolean isJcrType(String name);

	boolean isSubtype(String supertype, String subtype);
}
