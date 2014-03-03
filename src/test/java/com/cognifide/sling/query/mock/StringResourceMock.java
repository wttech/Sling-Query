package com.cognifide.sling.query.mock;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;

public class StringResourceMock implements Resource {

	private final String name;

	private final Resource parent;

	private final String value;

	public StringResourceMock(Resource parent, String name, String value) {
		this.parent = parent;
		this.name = name;
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
		if (type.isAssignableFrom(String.class)) {
			return (AdapterType) value;
		} else {
			return null;
		}
	}

	@Override
	public String getPath() {
		if (parent == null) {
			return "";
		} else {
			return String.format("%s/%s", parent.getPath(), name);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Resource getParent() {
		return parent;
	}

	@Override
	public Iterator<Resource> listChildren() {
		return Arrays.<Resource> asList().iterator();
	}

	@Override
	public Resource getChild(String relPath) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getResourceType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getResourceSuperType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isResourceType(String resourceType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResourceMetadata getResourceMetadata() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResourceResolver getResourceResolver() {
		throw new UnsupportedOperationException();
	}

}
