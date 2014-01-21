package com.cognifide.sling.query.selector;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.cognifide.sling.query.predicate.PropertyPredicate;
import com.cognifide.sling.query.selector.parser.SelectorFunction;

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

	@Test
	public void parseFunctionWithFilter() {
		Selector selector = new Selector(":has([key=value])");
		Assert.assertEquals(Arrays.asList(f("has", "[key=value]")), selector.getFunctions());
	}

	@Test
	public void parseNestedFunction() {
		Selector selector = new Selector(":not(:has(cq:Page))");
		Assert.assertEquals(Arrays.asList(f("not", ":has(cq:Page)")), selector.getFunctions());
	}

	@Test
	public void parseFunctionWithoutArgument() {
		Selector selector = new Selector(":first");
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parseFunctions() {
		Selector selector = new Selector(":eq(12):first");
		Assert.assertEquals(Arrays.asList(f("eq", "12"), f("first", null)), selector.getFunctions());
	}

	@Test
	public void parsePrimaryTypeAndFunction() {
		Selector selector = new Selector("cq:Page:first");
		Assert.assertEquals(selector.getResourceType(), "cq:Page");
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parsePrimaryTypeAndFunctions() {
		Selector selector = new Selector("cq:Page:first:eq(12)");
		Assert.assertEquals(selector.getResourceType(), "cq:Page");
		Assert.assertEquals(Arrays.asList(f("first", null), f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndFunction() {
		Selector selector = new Selector("my/resource/type:first");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndFunctions() {
		Selector selector = new Selector("my/resource/type:first:eq(12)");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
		Assert.assertEquals(Arrays.asList(f("first", null), f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndPropertyAndFunction() {
		Selector selector = new Selector("my/resource/type[key=value]:first");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
		Assert.assertEquals(Arrays.asList(pp("key", "value")), selector.getProperties());
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndPropertiesAndFunction() {
		Selector selector = new Selector("my/resource/type[key=value][key2=value2]:first");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
		Assert.assertEquals(Arrays.asList(pp("key", "value"), pp("key2", "value2")), selector.getProperties());
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndPropertyAndFunctions() {
		Selector selector = new Selector("my/resource/type[key=value]:first:eq(12)");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
		Assert.assertEquals(Arrays.asList(pp("key", "value")), selector.getProperties());
		Assert.assertEquals(Arrays.asList(f("first", null), f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndPropertiesAndFunctions() {
		Selector selector = new Selector("my/resource/type[key=value][key2=value2]:first:eq(12)");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
		Assert.assertEquals(Arrays.asList(pp("key", "value"), pp("key2", "value2")), selector.getProperties());
		Assert.assertEquals(Arrays.asList(f("first", null), f("eq", "12")), selector.getFunctions());
	}

	private static PropertyPredicate pp(String key, String value) {
		return new PropertyPredicate(key, value);
	}

	private static SelectorFunction f(String functionId, String argument) {
		return new SelectorFunction(functionId, argument);
	}
}