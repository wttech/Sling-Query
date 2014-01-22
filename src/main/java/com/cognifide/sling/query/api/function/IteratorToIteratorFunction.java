package com.cognifide.sling.query.api.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;

/**
 * A {@link Function} that takes a {@link Resource} {@link Iterator} and returns another {@link Iterator}.
 * 
 * @author Tomasz Rękawek
 * 
 */
public interface IteratorToIteratorFunction extends Function<Iterator<Resource>, Iterator<Resource>> {

	/**
	 * Transform one iterator into another.
	 * 
	 */
	Iterator<Resource> apply(Iterator<Resource> input);

}
