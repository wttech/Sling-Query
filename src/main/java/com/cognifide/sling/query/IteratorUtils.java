package com.cognifide.sling.query;

import java.util.Arrays;
import java.util.Iterator;

public class IteratorUtils {

	private IteratorUtils() {
	}

	public static <T> Iterator<T> arrayIterator(T... elements) {
		return Arrays.asList(elements).iterator();
	}

	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> singleElementIterator(T element) {
		return Arrays.asList(element).iterator();
	}

	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> emptyIterator() {
		return Arrays.<T> asList().iterator();
	}
}
