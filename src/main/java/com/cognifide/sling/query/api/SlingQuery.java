package com.cognifide.sling.query.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.Operation;
import com.cognifide.sling.query.function.ChildrenFunction;
import com.cognifide.sling.query.function.ClosestFunction;
import com.cognifide.sling.query.function.HasFunction;
import com.cognifide.sling.query.function.IdentityFunction;
import com.cognifide.sling.query.function.FindFunction;
import com.cognifide.sling.query.function.LastFunction;
import com.cognifide.sling.query.function.NextFunction;
import com.cognifide.sling.query.function.ParentFunction;
import com.cognifide.sling.query.function.ParentsFunction;
import com.cognifide.sling.query.function.PrevFunction;
import com.cognifide.sling.query.function.SiblingsFunction;
import com.cognifide.sling.query.function.SliceFunction;
import com.cognifide.sling.query.predicate.RejectingPredicate;
import com.cognifide.sling.query.selector.NotFunction;
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

	private SlingQuery(SlingQuery original) {
		this.operations.addAll(original.operations);
		this.resources = new ArrayList<Resource>(original.resources);
	}

	@Override
	public Iterator<Resource> iterator() {
		Iterator<Resource> iterator = resources.iterator();
		for (Operation operation : operations) {
			iterator = operation.getIterator(iterator);
		}
		return iterator;
	}

	public SlingQuery children() {
		return children("");
	}

	public SlingQuery children(String selector) {
		return function(new ChildrenFunction(), selector);
	}

	public SlingQuery closest(String selector) {
		return function(new ClosestFunction(new Selector(selector).getPredicate()), "");
	}

	public SlingQuery eq(int index) {
		return slice(index, index);
	}

	public SlingQuery filter(ResourcePredicate predicate) {
		return addOperation(new Operation(new IdentityFunction(), predicate));
	}

	public SlingQuery find() {
		return find("");
	}

	public SlingQuery find(String selector) {
		return function(new FindFunction(), selector);
	}

	public SlingQuery first() {
		return eq(0);
	}

	public SlingQuery function(Function<?, ?> function, String selector) {
		return addOperation(new Operation(function, selector));
	}

	public SlingQuery has(String selector) {
		return function(new HasFunction(new Selector(selector).getPredicate()), "");
	}

	public SlingQuery last() {
		return function(new LastFunction(), "");
	}

	public SlingQuery next() {
		return next("");
	}

	public SlingQuery next(String selector) {
		return function(new NextFunction(null), selector);
	}

	public SlingQuery nextAll() {
		return nextAll("");
	}

	public SlingQuery nextAll(String selector) {
		return function(new NextFunction(new RejectingPredicate()), selector);
	}

	public SlingQuery nextUntil(String until) {
		return nextUntil(until, "");
	}

	public SlingQuery nextUntil(String until, String selector) {
		return function(new NextFunction(new Selector(until).getPredicate()), selector);
	}

	public SlingQuery not(String selector) {
		return function(new NotFunction(new Selector(selector)), "");
	}

	public SlingQuery parent() {
		return function(new ParentFunction(), "");
	}

	public SlingQuery parents() {
		return parents("");
	}

	public SlingQuery parents(String selector) {
		return function(new ParentsFunction(), selector);
	}

	public SlingQuery prev() {
		return prev("");
	}

	public SlingQuery prev(String selector) {
		return function(new PrevFunction(null), selector);
	}

	public SlingQuery prevAll() {
		return prevAll("");
	}

	public SlingQuery prevAll(String selector) {
		return function(new PrevFunction(new RejectingPredicate()), selector);
	}

	public SlingQuery prevUntil(String until) {
		return prevUntil(until, "");
	}

	public SlingQuery prevUntil(String until, String selector) {
		return function(new PrevFunction(new Selector(until).getPredicate()), selector);
	}

	public SlingQuery siblings() {
		return siblings("");
	}

	public SlingQuery siblings(String selector) {
		return function(new SiblingsFunction(), selector);
	}

	public SlingQuery slice(int from) {
		if (from < 0) {
			throw new IndexOutOfBoundsException();
		}
		return function(new SliceFunction(from), "");
	}

	public SlingQuery slice(int from, int to) {
		if (from < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (from > to) {
			throw new IllegalArgumentException();
		}
		return function(new SliceFunction(from, to), "");
	}

	private SlingQuery addOperation(Operation o) {
		SlingQuery newQuery = new SlingQuery(this);
		newQuery.operations.add(o);
		return newQuery;
	}
}