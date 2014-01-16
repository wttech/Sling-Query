package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.Operation;

public class OperationIterator extends AbstractResourceIterator {

	private final Operation operation;

	private final Iterator<Resource> parentIterator;

	private Iterator<Resource> operationIterator;

	public OperationIterator(Operation operation, Iterator<Resource> parentIterator) {
		this.operation = operation;
		this.parentIterator = parentIterator;
	}

	@Override
	protected Resource getResource() {
		if (operationIterator != null && operationIterator.hasNext()) {
			return operationIterator.next();
		}
		while (parentIterator.hasNext()) {
			Resource parentResource = parentIterator.next();
			operationIterator = operation.getResources(parentResource);
			if (operationIterator.hasNext()) {
				return operationIterator.next();
			}
		}
		return null;
	}

}
