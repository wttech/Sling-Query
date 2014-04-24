package com.cognifide.sling.query.resource.jcr;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeTypeManager;

import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionJcrTypeResolver implements JcrTypeResolver {

	private static final Logger LOG = LoggerFactory.getLogger(SessionJcrTypeResolver.class);

	private final NodeTypeManager nodeTypeManager;

	public SessionJcrTypeResolver(ResourceResolver resolver) {
		NodeTypeManager m = null;
		try {
			if (resolver != null) {
				m = resolver.adaptTo(Session.class).getWorkspace().getNodeTypeManager();
			}
		} catch (RepositoryException e) {
			LOG.error("Can't get node type manager", e);
			m = null;
		}
		nodeTypeManager = m;
	}

	@Override
	public boolean isJcrType(String name) {
		if (nodeTypeManager == null) {
			return false;
		}
		if (name == null || name.contains("/")) {
			return false;
		}
		try {
			return nodeTypeManager.hasNodeType(name);
		} catch (RepositoryException e) {
			LOG.error("Can't check node type " + name, e);
			return false;
		}
	}

	@Override
	public boolean isSubtype(String supertype, String subtype) {
		if (nodeTypeManager == null) {
			return false;
		}
		if (!isJcrType(subtype) || !isJcrType(supertype)) {
			return false;
		}
		try {
			return nodeTypeManager.getNodeType(subtype).isNodeType(supertype);
		} catch (RepositoryException e) {
			LOG.error("Can't compare two node types: " + subtype + " and " + supertype, e);
			return false;
		}
	}

}
