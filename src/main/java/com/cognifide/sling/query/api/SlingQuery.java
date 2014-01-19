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
import com.cognifide.sling.query.predicate.FilterPredicate;
import com.cognifide.sling.query.predicate.RejectingPredicate;

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

	public SlingQuery closest(String filter) {
		function(new ClosestFunction(new FilterPredicate(filter)), filter);
		return this;
	}

	public SlingQuery children() {
		children("");
		return this;
	}

	public SlingQuery children(String filter) {
		function(new ChildrenFunction(), filter);
		return this;
	}

	public SlingQuery siblings(String filter) {
		function(new SiblingsFunction(), filter);
		return this;
	}

	public SlingQuery siblings() {
		siblings("");
		return this;
	}

	public SlingQuery find(String filter) {
		function(new FindFunction(), filter);
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

	public SlingQuery parents(String filter) {
		function(new ParentsFunction(), filter);
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
		function(new NextFunction(null), filter);
		return this;
	}

	public SlingQuery nextAll() {
		nextAll("");
		return this;
	}

	public SlingQuery nextAll(String filter) {
		function(new NextFunction(new RejectingPredicate()), filter);
		return this;
	}

	public SlingQuery nextUntil(String until) {
		nextUntil(until, "");
		return this;
	}

	public SlingQuery nextUntil(String until, String filter) {
		function(new NextFunction(new FilterPredicate(until)), filter);
		return this;
	}

	public SlingQuery prev() {
		prev("");
		return this;
	}

	public SlingQuery prev(String filter) {
		function(new PrevFunction(null), filter);
		return this;
	}

	public SlingQuery prevAll() {
		prevAll("");
		return this;
	}

	public SlingQuery prevAll(String filter) {
		function(new PrevFunction(new RejectingPredicate()), filter);
		return this;
	}

	public SlingQuery prevUntil(String until) {
		prevUntil(until, "");
		return this;
	}

	public SlingQuery prevUntil(String until, String filter) {
		function(new PrevFunction(new FilterPredicate(until)), filter);
		return this;
	}

	public SlingQuery function(Function<?, ?> function, String filter) {
		operations.add(new Operation(function, filter));
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