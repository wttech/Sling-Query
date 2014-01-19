package com.cognifide.sling.query.selector;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.cognifide.sling.query.predicate.PropertyPredicate;

public class SelectorTest {
	@Test
	public void parseResourceType() {
		Selector selector = new Selector("my/resource/type");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
	}

	@Test
	public void parseProperty() {
		Selector selector = new Selector("[key=value]");
		Assert.assertEquals(Arrays.asList(pp("key", "value")), selector.getProperties());
	}

	@Test
	public void parseProperties() {
		Selector selector = new Selector("[key=value][key2=value2]");
		Assert.assertEquals(Arrays.asList(pp("key", "value"), pp("key2", "value2")), selector.getProperties());
	}

	@Test
	public void parseResourceTypeAndProperty() {
		Selector selector = new Selector("my/resource/type[key=value]");
		Assert.assertEquals(Arrays.asList(pp("key", "value")), selector.getProperties());
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
	}

	@Test
	public void parseResourceTypeAndProperties() {
		Selector selector = new Selector("my/resource/type[key=value][key2=value2]");
		Assert.assertEquals(Arrays.asList(pp("key", "value"), pp("key2", "value2")), selector.getProperties());
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
	}

	@Test
	public void parseFunction() {
		Selector selector = new Selector(":eq(12)");
		Assert.assertEquals(Arrays.asList(f("eq", "12")), selector.getFunctions());

	}

	private static PropertyPredicate pp(String key, String value) {
		return new PropertyPredicate(key, value);
	}

	private static ParsedSelectorFunction f(String functionId, String argument) {
		return new ParsedSelectorFunction(functionId, argument);
	}
}
