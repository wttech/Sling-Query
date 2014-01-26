package com.cognifide.sling.query;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;

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

	public static List<Resource> iteratorToList(Iterator<Resource> iterator) {
		List<Resource> list = new ArrayList<Resource>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	public static void assertEmptyIterator(Iterator<Resource> iterator) {
		if (iterator.hasNext()) {
			Assert.fail(String.format("Iterator should be empty, but %s is returned", iterator.next()
					.toString()));
		}
	}

	public static void assertResourceSetEquals(Iterator<Resource> iterator, String... names) {
		List<String> namesSet = new LinkedList<String>(Arrays.asList(names));
		while (iterator.hasNext()) {
			Resource resource = iterator.next();
			if (!namesSet.remove(resource.getName())) {
				Assert.fail(String.format("Unexpected resource %s", resource.getName()));
			}
		}
		if (!namesSet.isEmpty()) {
			Assert.fail("Following resources not found: " + namesSet.toString());
		}
	}
}
