package com.cognifide.sling.query.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Assert;
import org.junit.Test;

import com.cognifide.sling.query.TestUtils;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.function.IdentityFunction;
import com.cognifide.sling.query.function.SliceFunction;

public class ReverseTest {

	@Test
	public void testIdentity() {
		Iterator<Option<String>> result = reverse(iterator("a", "b", "c", "d", "e"),
				new IdentityFunction<String>());
		assertEquals(result, null, null, null, null, null);
	}

	@Test
	public void testFirst() {
		Iterator<Option<String>> result = reverse(iterator("a", "b", "c", "d", "e"),
				new SliceFunction<String>(1));
		assertEquals(result, "a", null, null, null, null);
	}

	@Test
	public void testLast() {
		Iterator<Option<String>> result = reverse(iterator("a", "b", "c", "d", "e"),
				new SliceFunction<String>(0, 3));
		assertEquals(result, null, null, null, null, "e");
	}

	private Iterator<Option<String>> reverse(ListIterator<Option<String>> iterator,
			IteratorToIteratorFunction<String> function) {
		return new ReverseIterator<String>(function, iterator);
	}

	private ListIterator<Option<String>> iterator(String... args) {
		List<Option<String>> list = new ArrayList<Option<String>>(args.length);
		for (String a : args) {
			list.add(Option.of(a));
		}
		return list.listIterator();
	}

	private void assertEquals(Iterator<Option<String>> actual, String... args) {
		List<Option<String>> actualList = TestUtils.iteratorToList(actual);
		List<Option<String>> expectedList = TestUtils.iteratorToList(iterator(args));
		Assert.assertEquals(expectedList, actualList);
	}

}
