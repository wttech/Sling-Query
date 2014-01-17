package com.cognifide.sling.query.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceProvider;

import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.iterator.FunctionIterator;
import com.cognifide.sling.query.function.ChildrenFunction;
import com.cognifide.sling.query.function.ClosestFunction;
import com.cognifide.sling.query.function.FilterFunction;
import com.cognifide.sling.query.function.FindFunction;
import com.cognifide.sling.query.function.ParentFunction;
import com.cognifide.sling.query.predicate.FilterPredicate;

public class SlingQuery implements Iterable<Resource> {
	private final List<Function<?, ?>> functions = new ArrayList<Function<?, ?>>();

	private final List<Resource> resources;

	public static SlingQuery $(Resource... resources) {
		return new SlingQuery(resources);
	}

	private SlingQuery(Resource... resources) {
		this.resources = Arrays.asList(resources);
	}

	public SlingQuery parent() {
		functions.add(new ParentFunction());
		return this;
	}

	public SlingQuery closest(String filter) {
		functions.add(new ClosestFunction(new FilterPredicate(filter)));
		return this;
	}

	public SlingQuery children() {
		children("");
		return this;
	}

	public SlingQuery children(String filter) {
		functions.add(new ChildrenFunction(new FilterPredicate(filter)));
		return this;
	}

	public SlingQuery siblings(String filter) {
		parent();
		children(filter);
		return this;
	}

	public SlingQuery siblings() {
		siblings("");
		return this;
	}

	public SlingQuery find(String filter) {
		functions.add(new FindFunction(new FilterPredicate(filter)));
		return this;
	}

	public SlingQuery find() {
		find("");
		return this;
	}

	public SlingQuery filter(ResourcePredicate predicate) {
		functions.add(new FilterFunction(predicate));
		return this;
	}

	@Override
	public Iterator<Resource> iterator() {
		Iterator<Resource> iterator = resources.iterator();
		for (Function<?, ?> function : functions) {
			if (function instanceof ResourceProvider) {
			} else if (function instanceof ResourceToIteratorFunction) {
				iterator = new FunctionIterator((ResourceToIteratorFunction) function, iterator);
			} else if (function instanceof IteratorToIteratorFunction) {
				iterator = ((IteratorToIteratorFunction) function).apply(iterator);
			}
		}
		return iterator;
	}
}