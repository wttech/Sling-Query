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
public interface IteratorToIteratorFunction<T> extends Function<Iterator<T>, Iterator<T>> {

	/**
	 * Transform one iterator into another.
	 * 
	 */
	Iterator<T> apply(Iterator<T> input);

}
