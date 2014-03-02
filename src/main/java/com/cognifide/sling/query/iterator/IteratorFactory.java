package com.cognifide.sling.query.iterator;

import java.util.Iterator;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.api.function.ElementToElementFunction;
import com.cognifide.sling.query.function.ResourceToIteratorWrapperFunction;
import com.cognifide.sling.query.selector.Option;

public final class IteratorFactory {
	private IteratorFactory() {
	}

	@SuppressWarnings("unchecked")
	public static <T> Iterator<Option<T>> getOptionIterator(Function<?, ?> function,
			Iterator<Option<T>> parentIterator) {
		if (isEtoE(function)) {
			return getOptionIterator((ElementToElementFunction<T>) function, parentIterator);
		} else if (isEtoI(function)) {
			return getOptionIterator((ElementToIteratorFunction<T>) function, parentIterator);
		} else if (isOptionItoI(function)) {
			return getOptionIterator((OptionIteratorToIteratorFunction<T>) function, parentIterator);
		} else if (isItoI(function)) {
			throw new IllegalArgumentException("Function " + function.toString() + " is not supported");
		} else {
			throw new IllegalArgumentException("Don't know how to handle " + function.toString());
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> getIterator(Function<?, ?> function, Iterator<T> parentIterator) {
		if (isEtoE(function)) {
			return getIterator((ElementToElementFunction<T>) function, parentIterator);
		} else if (isEtoI(function)) {
			return getIterator((ElementToIteratorFunction<T>) function, parentIterator);
		} else if (isOptionItoI(function)) {
			return getIterator((OptionIteratorToIteratorFunction<T>) function, parentIterator);
		} else if (isItoI(function)) {
			return getIterator((IteratorToIteratorFunction<T>) function, parentIterator);
		} else {
			throw new IllegalArgumentException("Don't know how to handle " + function.toString());
		}
	}

	private static <T> Iterator<T> getIterator(ElementToElementFunction<T> function,
			Iterator<T> parentIterator) {
		ElementToIteratorFunction<T> wrappingFunction = new ResourceToIteratorWrapperFunction<T>(function);
		return new FunctionIterator<T>(wrappingFunction, parentIterator);
	}

	private static <T> Iterator<T> getIterator(ElementToIteratorFunction<T> function,
			Iterator<T> parentIterator) {
		return new FunctionIterator<T>((ElementToIteratorFunction<T>) function, parentIterator);
	}

	private static <T> Iterator<T> getIterator(OptionIteratorToIteratorFunction<T> function,
			Iterator<T> parentIterator) {
		return new EmptyElementFilter<T>(function.apply(new OptionalElementIterator<T>(parentIterator)));
	}

	private static <T> Iterator<Option<T>> getOptionIterator(ElementToElementFunction<T> function,
			Iterator<Option<T>> parentIterator) {
		ElementToIteratorFunction<T> wrappingFunction = new ResourceToIteratorWrapperFunction<T>(function);
		return new OptionFunctionIterator<T>(wrappingFunction, parentIterator);
	}

	private static <T> Iterator<Option<T>> getOptionIterator(ElementToIteratorFunction<T> function,
			Iterator<Option<T>> parentIterator) {
		return new OptionFunctionIterator<T>((ElementToIteratorFunction<T>) function, parentIterator);
	}

	private static <T> Iterator<Option<T>> getOptionIterator(OptionIteratorToIteratorFunction<T> function,
			Iterator<Option<T>> parentIterator) {
		return function.apply(parentIterator);
	}

	private static <T> Iterator<T> getIterator(IteratorToIteratorFunction<T> function,
			Iterator<T> parentIterator) {
		return function.apply(parentIterator);
	}

	private static boolean isEtoE(Function<?, ?> function) {
		return function instanceof ElementToElementFunction;
	}

	private static boolean isEtoI(Function<?, ?> function) {
		return function instanceof ElementToIteratorFunction;
	}

	private static boolean isOptionItoI(Function<?, ?> function) {
		return function instanceof OptionIteratorToIteratorFunction;
	}

	private static boolean isItoI(Function<?, ?> function) {
		return function instanceof IteratorToIteratorFunction;
	}

}
