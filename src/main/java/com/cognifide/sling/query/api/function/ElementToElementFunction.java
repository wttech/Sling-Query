package com.cognifide.sling.query.api.function;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.selector.OptionalElement;

/**
 * A {@link Function} that takes a {@link Resource} and returns {@link Resource}.
 * 
 * @author Tomasz Rękawek
 * 
 */
public interface ElementToElementFunction<T> extends Function<T, OptionalElement<T>> {

	/**
	 * Transform {@link Resource} into another {@link Resource}.
	 */
	OptionalElement<T> apply(T input);

}