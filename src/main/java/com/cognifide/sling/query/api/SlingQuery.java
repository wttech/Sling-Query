package com.cognifide.sling.query.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.Operation;
import com.cognifide.sling.query.ResourcePredicate;
import com.cognifide.sling.query.iterator.OperationIterator;
import com.cognifide.sling.query.operation.ChildrenOperation;
import com.cognifide.sling.query.operation.ClosestOperation;
import com.cognifide.sling.query.operation.FilterOperation;
import com.cognifide.sling.query.operation.FindOperation;
import com.cognifide.sling.query.operation.ParentOperation;
import com.cognifide.sling.query.predicate.FilterPredicate;

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
		operations.add(new ParentOperation());
		return this;
	}

	public SlingQuery closest(String filter) {
		operations.add(new ClosestOperation(new FilterPredicate(filter)));
		return this;
	}

	public SlingQuery children() {
		children("");
		return this;
	}

	public SlingQuery children(String filter) {
		operations.add(new ChildrenOperation(new FilterPredicate(filter)));
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
		operations.add(new FindOperation(new FilterPredicate(filter)));
		return this;
	}
	
	public SlingQuery find() {
		find("");
		return this;
	}

	public SlingQuery filter(ResourcePredicate predicate) {
		operations.add(new FilterOperation(predicate));
		return this;
	}

	@Override
	public Iterator<Resource> iterator() {
		Iterator<Resource> iterator = resources.iterator();
		for (Operation operation : operations) {
			iterator = new OperationIterator(operation, iterator);
		}
		return iterator;
	}
}
