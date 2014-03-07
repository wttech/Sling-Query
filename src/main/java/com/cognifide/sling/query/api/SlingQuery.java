package com.cognifide.sling.query.api;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.sling.query.JavaQuery;
import com.cognifide.sling.query.resource.ResourceTreeProvider;

/**
 * SlingQuery is a Sling resource tree traversal tool inspired by the jQuery. Full documentation could be
 * found on the <a href="https://github.com/Cognifide/Sling-Query">Github project page</a>.
 * 
 * @author Tomasz Rękawek
 */
public class SlingQuery extends JavaQuery<Resource, SlingQuery> {

	private SlingQuery(JavaQuery<Resource, SlingQuery> original, SearchStrategy strategy) {
		super(original, strategy);
	}

	private SlingQuery(Resource... resources) {
		super(new ResourceTreeProvider(), resources);
	}

	public static SlingQuery $(Resource... resources) {
		return new SlingQuery(resources);
	}

	public static SlingQuery $(ResourceResolver resolver) {
		return new SlingQuery(resolver.getResource("/"));
	}

	@Override
	protected SlingQuery clone(JavaQuery<Resource, SlingQuery> original, SearchStrategy strategy) {
		return new SlingQuery(original, strategy);
	}
}