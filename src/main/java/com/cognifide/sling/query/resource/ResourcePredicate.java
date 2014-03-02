package com.cognifide.sling.query.resource;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.selector.parser.Attribute;

public class ResourcePredicate implements Predicate<Resource> {

	private static final Logger LOG = LoggerFactory.getLogger(ResourcePredicate.class);

	private final String resourceType;

	private final String resourceName;

	private final List<Predicate<Resource>> subPredicates;

	public ResourcePredicate(String resourceType, String resourceName, List<Attribute> attributes) {
		this.resourceType = resourceType;
		this.resourceName = resourceName;
		this.subPredicates = new ArrayList<Predicate<Resource>>();
		for (Attribute a : attributes) {
			subPredicates.add(new ResourcePropertyPredicate(a));
		}
	}

	@Override
	public boolean accepts(Resource resource) {
		if (StringUtils.isNotBlank(resourceName) && !resource.getName().equals(resourceName)) {
			return false;
		}
		if (!isResourceType(resource, resourceType)) {
			return false;
		}
		for (Predicate<Resource> predicate : subPredicates) {
			if (!predicate.accepts(resource)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isResourceType(Resource resource, String resourceType) {
		if (StringUtils.isBlank(resourceType)) {
			return true;
		}
		if (resource.isResourceType(resourceType)) {
			return true;
		}
		if (resourceType.contains("/") || !resourceType.contains(":")) {
			return false;
		}
		Node node = resource.adaptTo(Node.class);
		try {
			if (node != null) {
				return node.isNodeType(resourceType);
			}
		} catch (RepositoryException e) {
			LOG.error("Can't check node type", e);
		}
		return false;
	}
}
