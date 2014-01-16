package com.cognifide.sling.query.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.sql.rowset.Predicate;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.FilterPredicate;
import com.cognifide.sling.query.Operation;
import com.cognifide.sling.query.OperationIterator;
import com.cognifide.sling.query.operation.ClosestOperation;
import com.cognifide.sling.query.operation.ParentOperation;

public class SlingQuery implements Iterable<Resource> {
	private final List<Operation> operations = new ArrayList<Operation>();

	private final List<Resource> resources;

	public static SlingQuery $(Resource... resources) {
		return new SlingQuery(resources);
	}

	private SlingQuery(Resource... resources) {
		this.resources = Arrays.asList(resources);
	}

	public SlingQuery closest(String filter) {
		operations.add(new ClosestOperation(new FilterPredicate(filter)));
		return this;
	}

	public SlingQuery parent() {
		operations.add(new ParentOperation());
		return this;
	}

	public SlingQuery children(String filter) {
		return this;
	}

	public SlingQuery find(String filter) {
		return this;
	}

	public SlingQuery filter(Predicate predicate) {
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
