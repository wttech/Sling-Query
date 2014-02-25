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
public interface ResourceToIteratorFunction<T> extends Function<T, Iterator<T>> {

	/**
	 * Transform {@link Resource} into {@link Iterator}.
	 */
	Iterator<T> apply(T input);

}
