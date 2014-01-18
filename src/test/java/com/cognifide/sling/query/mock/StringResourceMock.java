package com.cognifide.sling.query.mock;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.sling.query.iterator.EmptyIterator;

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
		throw new UnsupportedOperationException();
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
		return EmptyIterator.INSTANCE;
	}

	@Override
	public Resource getChild(String relPath) {
		return null;
	}

	@Override
	public String getResourceType() {
		return null;
	}

	@Override
	public String getResourceSuperType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isResourceType(String resourceType) {
		return false;
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
