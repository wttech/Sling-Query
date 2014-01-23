package com.cognifide.sling.query.selector;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.cognifide.sling.query.predicate.PropertyPredicate;
import com.cognifide.sling.query.selector.parser.SelectorFunction;
import com.cognifide.sling.query.selector.parser.SelectorParser;
import com.cognifide.sling.query.selector.parser.SelectorSegment;

public class SelectorTest {
	@Test
	public void parseResourceType() {
		SelectorSegment selector = getFirstSegment("my/resource/type");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
	}

	@Test
	public void parseProperty() {
		SelectorSegment selector = getFirstSegment("[key=value]");
		Assert.assertEquals(Arrays.asList(pp("key", "value")), selector.getAttributes());
	}

	@Test
	public void parseProperties() {
		SelectorSegment selector = getFirstSegment("[key=value][key2=value2]");
		Assert.assertEquals(Arrays.asList(pp("key", "value"), pp("key2", "value2")), selector.getAttributes());
	}

	@Test
	public void parseResourceTypeAndProperty() {
		SelectorSegment selector = getFirstSegment("my/resource/type[key=value]");
		Assert.assertEquals(Arrays.asList(pp("key", "value")), selector.getAttributes());
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
	}

	@Test
	public void parseResourceTypeAndProperties() {
		SelectorSegment selector = getFirstSegment("my/resource/type[key=value][key2=value2]");
		Assert.assertEquals(Arrays.asList(pp("key", "value"), pp("key2", "value2")), selector.getAttributes());
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
	}

	@Test
	public void parseFunction() {
		SelectorSegment selector = getFirstSegment(":eq(12)");
		Assert.assertEquals(Arrays.asList(f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseFunctionWithFilter() {
		SelectorSegment selector = getFirstSegment(":has([key=value])");
		Assert.assertEquals(Arrays.asList(f("has", "[key=value]")), selector.getFunctions());
	}

	@Test
	public void parseNestedFunction() {
		SelectorSegment selector = getFirstSegment(":not(:has(cq:Page))");
		Assert.assertEquals(Arrays.asList(f("not", ":has(cq:Page)")), selector.getFunctions());
	}

	@Test
	public void parseFunctionWithoutArgument() {
		SelectorSegment selector = getFirstSegment(":first");
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parseFunctions() {
		SelectorSegment selector = getFirstSegment(":eq(12):first");
		Assert.assertEquals(Arrays.asList(f("eq", "12"), f("first", null)), selector.getFunctions());
	}

	@Test
	public void parsePrimaryTypeAndFunction() {
		SelectorSegment selector = getFirstSegment("cq:Page:first");
		Assert.assertEquals(selector.getResourceType(), "cq:Page");
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parsePrimaryTypeAndFunctions() {
		SelectorSegment selector = getFirstSegment("cq:Page:first:eq(12)");
		Assert.assertEquals(selector.getResourceType(), "cq:Page");
		Assert.assertEquals(Arrays.asList(f("first", null), f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndFunction() {
		SelectorSegment selector = getFirstSegment("my/resource/type:first");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndFunctions() {
		SelectorSegment selector = getFirstSegment("my/resource/type:first:eq(12)");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
		Assert.assertEquals(Arrays.asList(f("first", null), f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndPropertyAndFunction() {
		SelectorSegment selector = getFirstSegment("my/resource/type[key=value]:first");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
		Assert.assertEquals(Arrays.asList(pp("key", "value")), selector.getAttributes());
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndPropertiesAndFunction() {
		SelectorSegment selector = getFirstSegment("my/resource/type[key=value][key2=value2]:first");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
		Assert.assertEquals(Arrays.asList(pp("key", "value"), pp("key2", "value2")), selector.getAttributes());
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndPropertyAndFunctions() {
		SelectorSegment selector = getFirstSegment("my/resource/type[key=value]:first:eq(12)");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
		Assert.assertEquals(Arrays.asList(pp("key", "value")), selector.getAttributes());
		Assert.assertEquals(Arrays.asList(f("first", null), f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndPropertiesAndFunctions() {
		SelectorSegment selector = getFirstSegment("my/resource/type[key=value][key2=value2]:first:eq(12)");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
		Assert.assertEquals(Arrays.asList(pp("key", "value"), pp("key2", "value2")), selector.getAttributes());
		Assert.assertEquals(Arrays.asList(f("first", null), f("eq", "12")), selector.getFunctions());
	}

	private static PropertyPredicate pp(String key, String value) {
		return new PropertyPredicate(key, value);
	}

	private static SelectorFunction f(String functionId, String argument) {
		return new SelectorFunction(functionId, argument);
	}

	private static SelectorSegment getFirstSegment(String selector) {
		return SelectorParser.parse(selector).getSegments().get(0);
	}
}