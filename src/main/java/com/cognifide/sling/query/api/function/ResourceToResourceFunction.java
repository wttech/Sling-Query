package com.cognifide.sling.query.api.function;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;

/**
 * A {@link Function} that takes a {@link Resource} and returns {@link Resource}.
 * 
 * @author Tomasz Rękawek
 * 
 */
public interface ResourceToResourceFunction extends Function<Resource, Resource> {

	/**
	 * Transform {@link Resource} into another {@link Resource}.
	 */
	Resource apply(Resource input);

}