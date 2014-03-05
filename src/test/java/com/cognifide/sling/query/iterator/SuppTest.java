package com.cognifide.sling.query.iterator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.cognifide.sling.query.TestUtils;

import static com.cognifide.sling.query.TestUtils.l;

import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.function.IdentityFunction;
import com.cognifide.sling.query.function.IteratorToIteratorFunctionWrapper;
import com.cognifide.sling.query.function.SliceFunction;

public class SuppTest {

	@Test
	public void testIdentity() {
		test(l("a", "b", "c", "d", "e"), l("a", "b", "c", "d", "e"), new IdentityFunction<String>());
	}

	@Test
	public void testNoFirst() {
		test(l("a", "b", "c", "d", "e"), l(null, "b", "c", "d", "e"), new SliceFunction<String>(1));
	}

	@Test
	public void testNoSecond() {
		testExpanding(l("a", "---"), l("a", null));
	}

	@Test
	public void testNoTwoFirst() {
		test(l("a", "b", "c", "d", "e"), l(null, null, "c", "d", "e"), new SliceFunction<String>(2));
	}

	@Test
	public void testNoLast() {
		test(l("a", "b", "c", "d", "e"), l("a", "b", "c", "d", null), new SliceFunction<String>(0, 3));
	}

	@Test
	public void testNoTwoLast() {
		test(l("a", "b", "c", "d", "e"), l("a", "b", "c", null, null), new SliceFunction<String>(0, 2));
	}

	@Test
	public void testJustFirst() {
		test(l("a", "b", "c", "d", "e"), l("a", null, null, null, null), new SliceFunction<String>(0, 0));
	}

	@Test
	public void testExpandFirst() {
		testExpanding(l("+", "b", "c", "d", "e"), l("+", "b", "c", "d", "e"));
	}

	@Test
	public void testExpandMiddle() {
		testExpanding(l("a", "b", "+", "d", "e"), l("a", "b", "+", "d", "e"));
	}

	@Test
	public void testExpandLast() {
		testExpanding(l("a", "b", "c", "d", "+"), l("a", "b", "c", "d", "+"));
	}

	@Test
	public void testRemoveFirst() {
		testExpanding(l("-", "b", "c", "d", "e"), l(null, "b", "c", "d", "e"));
		testExpanding(l("---", "b", "c", "d", "e"), l(null, "b", "c", "d", "e"));
	}

	@Test
	public void testRemoveMiddle() {
		testExpanding(l("a", "b", "-", "d", "e"), l("a", "b", null, "d", "e"));
		testExpanding(l("a", "b", "---", "d", "e"), l("a", "b", null, "d", "e"));
	}

	@Test
	public void testRemoveLast() {
		testExpanding(l("a", "b", "c", "d", "-"), l("a", "b", "c", "d", null));
		testExpanding(l("a", "b", "c", "d", "---"), l("a", "b", "c", "d", null));
	}

	private static void testExpanding(List<String> input, List<String> output) {
		test(input, output, EXPANDING_FUNCTION);
	}

	private static <T> void test(List<T> input, List<T> output, IteratorToIteratorFunction<T> function) {
		List<Option<T>> optionInput = TestUtils.optionList(input);
		List<Option<T>> expectedOutput = TestUtils.optionList(output);
		Iterator<Option<T>> actualOutputIterator = new SuppIterator<T>(optionInput, function);
		List<Option<T>> actualOutput = TestUtils.iteratorToList(actualOutputIterator);
		Assert.assertEquals(expectedOutput, actualOutput);
	}

	private static final IteratorToIteratorFunctionWrapper<String> EXPANDING_FUNCTION = new IteratorToIteratorFunctionWrapper<String>(
			new ElementToIteratorFunction<String>() {
				@Override
				public Iterator<String> apply(String input) {
					if ("+".equals(input)) {
						return Arrays.asList("a", "b", "c").iterator();
					} else if ("-".equals(input)) {
						return Arrays.<String> asList().iterator();
					} else if ("---".equals(input)) {
						return Arrays.<String> asList(null, null, null).iterator();
					} else {
						return Arrays.asList(input).iterator();
					}
				}
			});
}