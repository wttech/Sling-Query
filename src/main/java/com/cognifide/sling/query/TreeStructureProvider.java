package com.cognifide.sling.query;

import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.predicate.PropertyPredicate;

public interface TreeStructureProvider<T> {
	Iterator<T> getChildren(T parent);

	T getParent(T element);

	String getName(T element);

	Predicate<T> getPredicate(String type, String id, List<PropertyPredicate> attributes);
	
	Iterator<T> query(String selector, T resource);
}
