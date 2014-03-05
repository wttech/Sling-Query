package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.cognifide.sling.query.TestUtils;

import static com.cognifide.sling.query.TestUtils.l;

import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.function.IdentityFunction;
import com.cognifide.sling.query.function.SliceFunction;

public class ReverseTest {

	@Test
	public void testReverse() {
		test(l("a", "b", "c", "d", "e"), l(null, null, null, null, null), new IdentityFunction<String>());
		test(l("a", "b", "c", "d", "e"), l("a", null, null, null, null), new SliceFunction<String>(1));
		test(l("a", "b", "c", "d", "e"), l(null, null, null, "d", "e"), new SliceFunction<String>(0, 2));
	}

	private static <T> void test(List<T> input, List<T> output, IteratorToIteratorFunction<T> function) {
		List<Option<T>> optionInput = TestUtils.optionList(input);
		List<Option<T>> expectedOutput = TestUtils.optionList(output);
		Iterator<Option<T>> actualOutputIterator = new ReverseIterator<T>(function, optionInput.iterator());
		List<Option<T>> actualOutput = TestUtils.iteratorToList(actualOutputIterator);
		Assert.assertEquals(expectedOutput, actualOutput);
	}

}
