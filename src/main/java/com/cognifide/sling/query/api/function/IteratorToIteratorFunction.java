package com.cognifide.sling.query.api.function;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.api.Function;

public interface IteratorToIteratorFunction extends Function<Iterator<Resource>, Iterator<Resource>> {

	Iterator<Resource> apply(Iterator<Resource> input);

}
