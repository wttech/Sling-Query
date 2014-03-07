package com.cognifide.sling.query.api;

/**
 * ResourcePredicate determine true or false value for a given T.
 * 
 * @author Tomasz Rękawek
 * 
 */
public interface Predicate<T> {
	/**
	 * Accept or reject given resource.
	 * 
	 * @param element Object to test
	 * @return {@code true} or {@code false}
	 */
	boolean accepts(T element);
}
