package com.cognifide.sling.query.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.IteratorFactory;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;
import com.cognifide.sling.query.function.ChildrenFunction;
import com.cognifide.sling.query.function.ClosestFunction;
import com.cognifide.sling.query.function.FilterFunction;
import com.cognifide.sling.query.function.FindFunction;
import com.cognifide.sling.query.function.NextFunction;
import com.cognifide.sling.query.function.ParentFunction;
import com.cognifide.sling.query.function.ParentsFunction;
import com.cognifide.sling.query.function.SliceFunction;
import com.cognifide.sling.query.predicate.FilterPredicate;
import com.cognifide.sling.query.predicate.RejectingPredicate;

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
		function(new ParentFunction());
		return this;
	}

	public SlingQuery closest(String filter) {
		function(new ClosestFunction(new FilterPredicate(filter)));
		return this;
	}

	public SlingQuery children() {
		children("");
		return this;
	}

	public SlingQuery children(String filter) {
		function(new ChildrenFunction(new FilterPredicate(filter)));
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
		function(new FindFunction(new FilterPredicate(filter)));
		return this;
	}

	public SlingQuery find() {
		find("");
		return this;
	}

	public SlingQuery filter(ResourcePredicate predicate) {
		function(new FilterFunction(predicate));
		return this;
	}

	public SlingQuery slice(int from, int to) {
		function(new SliceFunction(from, to));
		return this;
	}

	public SlingQuery slice(int from) {
		function(new SliceFunction(from));
		return this;
	}

	public SlingQuery parents(String filter) {
		function(new ParentsFunction(new FilterPredicate(filter)));
		return this;
	}

	public SlingQuery parents() {
		parents("");
		return this;
	}

	public SlingQuery next() {
		next("");
		return this;
	}

	public SlingQuery next(String filter) {
		function(new NextFunction(new FilterPredicate(filter)));
		return this;
	}

	public SlingQuery nextAll() {
		nextAll("");
		return this;
	}

	public SlingQuery nextAll(String filter) {
		function(new NextFunction(new FilterPredicate(filter), new RejectingPredicate()));
		return this;
	}

	public SlingQuery nextUntil(String until) {
		nextUntil(until, "");
		return this;
	}

	public SlingQuery nextUntil(String until, String filter) {
		function(new NextFunction(new FilterPredicate(filter), new FilterPredicate(until)));
		return this;
	}

	public SlingQuery function(ResourceToIteratorFunction function) {
		functions.add(function);
		return this;
	}

	public SlingQuery function(IteratorToIteratorFunction function) {
		functions.add(function);
		return this;
	}

	public SlingQuery function(ResourceToResourceFunction function) {
		functions.add(function);
		return this;
	}

	@Override
	public Iterator<Resource> iterator() {
		Iterator<Resource> iterator = resources.iterator();
		for (Function<?, ?> function : functions) {
			iterator = IteratorFactory.getIterator(function, iterator);
		}
		return iterator;
	}
}