package com.cognifide.sling.query;

import java.io.IOException;
import java.io.InputStream;

import org.apache.sling.api.resource.Resource;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cognifide.sling.query.mock.json.JsonToResource;

public class MyTest {

	private static Resource tree;

	@BeforeClass
	public static void setup() throws IOException {
		InputStream jsonStream = MyTest.class.getClassLoader().getResourceAsStream("sample_tree.json");
		try {
			tree = JsonToResource.parse(jsonStream);
		} finally {
			jsonStream.close();
		}
	}

	@Test
	public void test() {
		System.out.println(tree.getResourceType());
	}
}
