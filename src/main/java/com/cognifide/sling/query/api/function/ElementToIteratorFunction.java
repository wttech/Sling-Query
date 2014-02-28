package com.cognifide.sling.query.api.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.selector.OptionalElement;

/**
 * A {@link Function} that takes a {@link Resource} and returns {@link Iterator}.
 * 
 * @author Tomasz Rękawek
 * 
 */
public interface ElementToIteratorFunction<T> extends Function<T, Iterator<OptionalElement<T>>> {

	/**
	 * Transform {@link Resource} into {@link Iterator}.
	 */
	Iterator<OptionalElement<T>> apply(T input);

}
