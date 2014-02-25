package com.cognifide.sling.query.resource;

import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.TreeStructureProvider;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.iterator.tree.JcrTreeIterator;
import com.cognifide.sling.query.predicate.PropertyPredicate;

public class ResourceTreeProvider implements TreeStructureProvider<Resource> {

	@Override
	public Iterator<Resource> getChildren(Resource parent) {
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
	public Predicate<Resource> getPredicate(String type, String id, List<PropertyPredicate> attributes) {
		return new ResourcePredicate(type, id, attributes);
	}

	@Override
	public Iterator<Resource> query(String selector, Resource resource) {
		return new JcrTreeIterator(selector, resource);
	}

}
