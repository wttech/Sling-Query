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

public class NondegenerativeTest {

	@Test
	public void testIdentity() {
		Iterator<Option<String>> result = nondegenerative(iterator("a", "b", "c", "d", "e"),
				new IdentityFunction<String>());
		assertEquals(result, "a", "b", "c", "d", "e");
	}

	@Test
	public void testFirst() {
		Iterator<Option<String>> result = nondegenerative(iterator("a", "b", "c", "d", "e"),
				new SliceFunction<String>(1));
		assertEquals(result, null, "b", "c", "d", "e");
	}

	@Test
	public void testLast() {
		Iterator<Option<String>> result = nondegenerative(iterator("a", "b", "c", "d", "e"),
				new SliceFunction<String>(0, 3));
		assertEquals(result, "a", "b", "c", "d", null);
	}

	private Iterator<Option<String>> nondegenerative(ListIterator<Option<String>> iterator,
			IteratorToIteratorFunction<String> function) {
		return new SuppIterator<String>(iterator, function);
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
