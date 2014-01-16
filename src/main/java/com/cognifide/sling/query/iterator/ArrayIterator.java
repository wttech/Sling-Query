package com.cognifide.sling.query.iterator;

import org.apache.sling.api.resource.Resource;

public class ArrayIterator extends AbstractResourceIterator {

	private final Resource[] resources;

	private int index = 0;

	public ArrayIterator(Resource... resources) {
		this.resources = resources;
	}

	@Override
	protected Resource getResource() {
		if (index >= resources.length) {
			return null;
		} else {
			return resources[index++];
		}
	}

}
