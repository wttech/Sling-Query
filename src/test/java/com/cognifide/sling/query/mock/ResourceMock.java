package com.cognifide.sling.query.mock;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;

public class ResourceMock implements Resource {

	private final String name;

	private final String resourceType;

	private final Map<String, Resource> children;

	private final Map<String, String> properties;

	private final Resource parent;

	public ResourceMock(Resource parent, String name, String resourceType) {
		this.name = name;
		this.resourceType = resourceType;
		this.parent = parent;
		this.children = new LinkedHashMap<String, Resource>();
		this.properties = new LinkedHashMap<String, String>();
	}

	public void setProperty(String name, String value) {
		properties.put(name, value);
	}

	public void addChild(Resource resource) {
		children.put(resource.getName(), resource);
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
		return children.values().iterator();
	}

	@Override
	public Resource getChild(String relPath) {
		if (StringUtils.contains(relPath, '/')) {
			String firstPart = StringUtils.substringBefore(relPath, "/");
			String rest = StringUtils.substringAfter(relPath, "/");
			if (children.containsKey(firstPart)) {
				return children.get(firstPart).getChild(rest);
			}
		} else {
			if (children.containsKey(relPath)) {
				return children.get(relPath);
			} else if (properties.containsKey(relPath)) {
				return new StringResourceMock(this, relPath, properties.get(relPath));
			}
		}
		return null;
	}

	@Override
	public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
		return null;
	}

	@Override
	public boolean isResourceType(String resourceType) {
		return this.resourceType.equals(resourceType);
	}

	@Override
	public String getResourceType() {
		return resourceType;
	}

	@Override
	public String getPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getResourceSuperType() {
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
