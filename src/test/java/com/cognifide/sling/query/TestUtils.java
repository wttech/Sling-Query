package com.cognifide.sling.query;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;

import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.mock.json.JsonToResource;

public final class TestUtils {
	private TestUtils() {
	}

	public static Resource getTree() {
		InputStream jsonStream = TestUtils.class.getClassLoader().getResourceAsStream("sample_tree.json");
		try {
			Resource resource = JsonToResource.parse(jsonStream);
			jsonStream.close();
			return resource;
		} catch (IOException e) {
			return null;
		}
	}

	public static <T> List<T> iteratorToList(Iterator<T> iterator) {
		List<T> list = new ArrayList<T>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	public static <T> List<Option<T>> optionList(List<T> list) {
		List<Option<T>> result = new ArrayList<Option<T>>();
		int index = 0;
		for (T element : list) {
			result.add(Option.of(element, index++));
		}
		return result;
	}

	public static void assertEmptyIterator(Iterator<?> iterator) {
		if (iterator.hasNext()) {
			Assert.fail(String.format("Iterator should be empty, but %s is returned", iterator.next()
					.toString()));
		}
	}

	public static void assertResourceSetEquals(Iterator<Resource> iterator, String... names) {
		Set<String> expectedSet = new LinkedHashSet<String>(Arrays.asList(names));
		Set<String> actualSet = new LinkedHashSet<String>(getResourceNames(iterator));
		Assert.assertEquals(expectedSet, actualSet);
	}

	public static void assertResourceListEquals(Iterator<Resource> iterator, String... names) {
		Assert.assertEquals(Arrays.asList(names), getResourceNames(iterator));
	}

	public static List<String> l(String... args) {
		return Arrays.asList(args);
	}

	private static List<String> getResourceNames(Iterator<Resource> iterator) {
		List<String> resourceNames = new ArrayList<String>();
		while (iterator.hasNext()) {
			resourceNames.add(iterator.next().getName());
		}
		return resourceNames;
	}
}
