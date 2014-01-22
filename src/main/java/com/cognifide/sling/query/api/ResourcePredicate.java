package com.cognifide.sling.query.api;

import org.apache.sling.api.resource.Resource;

/**
 * ResourcePredicate determine true or false value for a given {@link Resource}.
 * 
 * @author Tomasz Rękawek
 * 
 */
public interface ResourcePredicate {
	/**
	 * Accept or reject given resource.
	 * 
	 * @param resource Object to test
	 * @return {@code true} or {@code false}
	 */
	boolean accepts(Resource resource);
}
