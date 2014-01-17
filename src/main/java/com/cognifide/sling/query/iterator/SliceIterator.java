package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

public class SliceIterator extends AbstractResourceIterator {

	private final Iterator<Resource> resources;

	private int current;

	private int from;

	private Integer to;

	public SliceIterator(Iterator<Resource> resources, int from, int to) {
		this.resources = resources;
		this.current = 0;
		this.from = from;
		this.to = to;
	}

	public SliceIterator(Iterator<Resource> resources, int from) {
		this.resources = resources;
		this.current = 0;
		this.from = from;
		this.to = null;
	}

	@Override
	protected Resource getResource() {
		if (to != null && current > to) {
			return null;
		}
		Resource resource;
		do {
			if (!resources.hasNext()) {
				return null;
			}
			resource = resources.next();
		} while (current++ < from);
		return resource;
	}
}