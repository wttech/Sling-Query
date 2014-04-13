package com.cognifide.sling.query.resource;

import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.resource.jcr.JcrQueryIterator;
import com.cognifide.sling.query.resource.jcr.JcrTypeResolver;
import com.cognifide.sling.query.resource.jcr.SessionJcrTypeResolver;
import com.cognifide.sling.query.selector.parser.Attribute;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class ResourceTreeProvider implements TreeProvider<Resource> {

	private final JcrTypeResolver typeResolver;

	public ResourceTreeProvider(ResourceResolver resolver) {
		this.typeResolver = new SessionJcrTypeResolver(resolver);
	}

	@Override
	public Iterator<Resource> listChildren(Resource parent) {
		return parent.listChildren();
	}

	@Override
	public Resource getParent(Resource element) {
		return element.getParent();
	}

	@Override
	public String getName(Resource element) {
		return element.getName();
	}

	@Override
	public Predicate<Resource> getPredicate(String type, String id, List<Attribute> attributes) {
		return new ResourcePredicate(type, id, attributes, typeResolver);
	}

	@Override
	public Iterator<Resource> query(List<SelectorSegment> segments, Resource resource) {
		return new JcrQueryIterator(segments, resource, typeResolver);
	}

	@Override
	public boolean sameElement(Resource o1, Resource o2) {
		if (o1 == null && o2 == null) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		return o1.getPath().equals(o2.getPath());
	}

	@Override
	public boolean isDescendant(Resource root, Resource testedElement) {
		if (root == null || testedElement == null) {
			return false;
		}
		return testedElement.getPath().startsWith(root.getPath());
	}

}
