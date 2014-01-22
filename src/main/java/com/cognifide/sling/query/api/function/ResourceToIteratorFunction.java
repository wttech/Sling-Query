package com.cognifide.sling.query.api.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;

/**
 * A {@link Function} that takes a {@link Resource} and returns {@link Iterator}.
 * 
 * @author Tomasz Rękawek
 * 
 */
public interface ResourceToIteratorFunction extends Function<Resource, Iterator<Resource>> {

	/**
	 * Transform {@link Resource} into {@link Iterator}.
	 */
	Iterator<Resource> apply(Resource input);

}
