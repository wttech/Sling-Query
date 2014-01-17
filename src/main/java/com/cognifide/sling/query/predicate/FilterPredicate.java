package com.cognifide.sling.query.predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.sling.query.ResourcePredicate;

public class FilterPredicate implements ResourcePredicate {

	private static final Logger LOG = LoggerFactory.getLogger(FilterPredicate.class);

	private String resourceType;

	private List<PropertyPredicate> properties = new ArrayList<PropertyPredicate>();

	public FilterPredicate(String filter) {
		if (StringUtils.isNotBlank(filter)) {
			parseFilter(filter);
		}
	}

	private void parseFilter(String filter) {
		Pattern pattern = Pattern.compile("^[^\\[]+");
		Matcher matcher = pattern.matcher(filter);
		if (matcher.find()) {
			resourceType = matcher.group();
		} else {
			resourceType = "";
		}
		pattern = Pattern.compile("\\[([^\\]]+)\\]");
		matcher = pattern.matcher(filter);
		if (matcher.find(resourceType.length())) {
			do {
				String[] split = matcher.group(1).split("=");
				if (split.length == 2) {
					properties.add(new PropertyPredicate(split[0], split[1]));
				}
			} while (matcher.find());
		}
	}

	@Override
	public boolean accepts(Resource resource) {
		if (!isResourceType(resource, resourceType)) {
			return false;
		}
		for (PropertyPredicate predicate : properties) {
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
