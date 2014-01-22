package com.cognifide.sling.query.api;

/**
 * Function can transform one value into another.
 * 
 * @author Tomasz Rękawek
 * 
 * @param <F> Input type
 * @param <T> Output type
 */
public interface Function<F, T> {
	/**
	 * Take input F and transform it into output T.
	 * 
	 * @param input Input value
	 * @return Output value
	 */
	T apply(F input);
}
