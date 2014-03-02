package com.cognifide.sling.query.api.function;

import java.util.Iterator;

import com.cognifide.sling.query.api.Function;

public interface IteratorToIteratorFunction<T> extends Function<Iterator<T>, Iterator<T>> {
}