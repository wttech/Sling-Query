package com.cognifide.sling.query.resource;

import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.resource.jcr.JcrQueryIterator;
import com.cognifide.sling.query.selector.parser.Attribute;

public class ResourceTreeProvider implements TreeProvider<Resource> {

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
		return new ResourcePredicate(type, id, attributes);
	}

	@Override
	public Iterator<Resource> query(String selector, Resource resource) {
		return new JcrQueryIterator(selector, resource);
	}

}
