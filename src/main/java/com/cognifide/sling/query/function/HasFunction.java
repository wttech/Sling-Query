package com.cognifide.sling.query.function;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.ResourcePredicate;

public class HasFunction implements Function<Resource, Resource> {

	private final ResourcePredicate predicate;

	public HasFunction(ResourcePredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public Resource apply(Resource input) {
		Deque<Resource> deque = new LinkedList<Resource>();
		deque.add(input);
		while (!deque.isEmpty()) {
			Iterator<Resource> children = deque.poll().listChildren();
			while (children.hasNext()) {
				Resource child = children.next();
				if (predicate.accepts(child)) {
					return input;
				}
				deque.add(child);
			}
		}
		return null;
	}

}
