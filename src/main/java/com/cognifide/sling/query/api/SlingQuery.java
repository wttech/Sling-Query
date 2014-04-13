package com.cognifide.sling.query.api;

import java.util.Iterator;

import org.apache.sling.api.adapter.Adaptable;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.sling.query.JavaQuery;
import com.cognifide.sling.query.iterator.AdaptToIterator;
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

	private SlingQuery(Resource[] resources, SearchStrategy strategy) {
		super(new ResourceTreeProvider(resources[0].getResourceResolver()), resources, strategy);
	}

	public static SlingQuery $(Resource... resources) {
		if (resources.length == 0) {
			throw new IllegalArgumentException("Initial collection can't be empty");
		} else {
			return new SlingQuery(resources, SearchStrategy.QUERY);
		}
	}

	public static SlingQuery $(ResourceResolver resolver) {
		return $(resolver.getResource("/"));
	}

	/**
	 * Transform the whole collection to a new {@link Iterable} object, invoking
	 * {@link Adaptable#adaptTo(Class)} method on each Resource. If some Resource can't be adapted to the
	 * class (eg. {@code adaptTo()} returns {@code null}), it will be skipped.
	 * 
	 * @param clazz Class used to adapt the Resources
	 * @return new iterable containing succesfully adapted Resources
	 */
	public <E> Iterable<E> map(final Class<? extends E> clazz) {
		return new Iterable<E>() {
			@Override
			public Iterator<E> iterator() {
				return new AdaptToIterator<Resource, E>(SlingQuery.this.iterator(), clazz);
			}
		};
	}

	@Override
	protected SlingQuery clone(JavaQuery<Resource, SlingQuery> original, SearchStrategy strategy) {
		return new SlingQuery(original, strategy);
	}
}