package com.cognifide.sling.query.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.Operation;
import com.cognifide.sling.query.function.ChildrenFunction;
import com.cognifide.sling.query.function.ClosestFunction;
import com.cognifide.sling.query.function.IdentityFunction;
import com.cognifide.sling.query.function.FindFunction;
import com.cognifide.sling.query.function.NextFunction;
import com.cognifide.sling.query.function.ParentFunction;
import com.cognifide.sling.query.function.ParentsFunction;
import com.cognifide.sling.query.function.PrevFunction;
import com.cognifide.sling.query.function.SiblingsFunction;
import com.cognifide.sling.query.function.SliceFunction;
import com.cognifide.sling.query.predicate.RejectingPredicate;
import com.cognifide.sling.query.selector.Selector;

public class SlingQuery implements Iterable<Resource> {
	private final List<Operation> operations = new ArrayList<Operation>();

	private final List<Resource> resources;

	public static SlingQuery $(Resource... resources) {
		return new SlingQuery(resources);
	}

	private SlingQuery(Resource... resources) {
		this.resources = Arrays.asList(resources);
	}

	public SlingQuery parent() {
		function(new ParentFunction(), "");
		return this;
	}

	public SlingQuery closest(String selector) {
		function(new ClosestFunction(new Selector(selector).getPredicate()), "");
		return this;
	}

	public SlingQuery children() {
		children("");
		return this;
	}

	public SlingQuery children(String selector) {
		function(new ChildrenFunction(), selector);
		return this;
	}

	public SlingQuery siblings(String selector) {
		function(new SiblingsFunction(), selector);
		return this;
	}

	public SlingQuery siblings() {
		siblings("");
		return this;
	}

	public SlingQuery find(String selector) {
		function(new FindFunction(), selector);
		return this;
	}

	public SlingQuery find() {
		find("");
		return this;
	}

	public SlingQuery filter(ResourcePredicate predicate) {
		operations.add(new Operation(new IdentityFunction(), predicate));
		return this;
	}

	public SlingQuery slice(int from, int to) {
		if (from < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (from > to) {
			throw new IllegalArgumentException();
		}
		function(new SliceFunction(from, to), "");
		return this;
	}

	public SlingQuery slice(int from) {
		if (from < 0) {
			throw new IndexOutOfBoundsException();
		}
		function(new SliceFunction(from), "");
		return this;
	}

	public SlingQuery parents(String selector) {
		function(new ParentsFunction(), selector);
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

	public SlingQuery next(String selector) {
		function(new NextFunction(null), selector);
		return this;
	}

	public SlingQuery nextAll() {
		nextAll("");
		return this;
	}

	public SlingQuery nextAll(String selector) {
		function(new NextFunction(new RejectingPredicate()), selector);
		return this;
	}

	public SlingQuery nextUntil(String until) {
		nextUntil(until, "");
		return this;
	}

	public SlingQuery nextUntil(String until, String selector) {
		function(new NextFunction(new Selector(until).getPredicate()), selector);
		return this;
	}

	public SlingQuery prev() {
		prev("");
		return this;
	}

	public SlingQuery prev(String selector) {
		function(new PrevFunction(null), selector);
		return this;
	}

	public SlingQuery prevAll() {
		prevAll("");
		return this;
	}

	public SlingQuery prevAll(String selector) {
		function(new PrevFunction(new RejectingPredicate()), selector);
		return this;
	}

	public SlingQuery prevUntil(String until) {
		prevUntil(until, "");
		return this;
	}

	public SlingQuery prevUntil(String until, String selector) {
		function(new PrevFunction(new Selector(until).getPredicate()), selector);
		return this;
	}

	public SlingQuery function(Function<?, ?> function, String selector) {
		operations.add(new Operation(function, selector));
		return this;
	}

	@Override
	public Iterator<Resource> iterator() {
		Iterator<Resource> iterator = resources.iterator();
		for (Operation operation : operations) {
			iterator = operation.getIterator(iterator);
		}
		return iterator;
	}
}