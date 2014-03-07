package com.cognifide.sling.query.api;

import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.selector.parser.Attribute;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public interface TreeProvider<T> {
	Iterator<T> listChildren(T parent);

	T getParent(T element);

	String getName(T element);

	Predicate<T> getPredicate(String type, String name, List<Attribute> attributes);

	Iterator<T> query(List<SelectorSegment> segment, T resource);

	boolean sameElement(T o1, T o2);
}
