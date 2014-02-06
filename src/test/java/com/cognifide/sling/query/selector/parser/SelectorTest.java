package com.cognifide.sling.query.selector.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.cognifide.sling.query.api.SearchStrategy;
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
	public void parseResourceTypeAndName() {
		SelectorSegment selector = getFirstSegment("my/resource/type#some-name");
		Assert.assertEquals("my/resource/type", selector.getResourceType());
		Assert.assertEquals("some-name", selector.getResourceName());
	}

	@Test
	public void parseResourceTypeAndProperty() {
		SelectorSegment selector = getFirstSegment("my/resource/type[key=value]");
		Assert.assertEquals(Arrays.asList(pp("key", "value")), selector.getAttributes());
		Assert.assertEquals("my/resource/type", selector.getResourceType());
	}

	@Test
	public void parseResourceTypeAndNameAndProperty() {
		SelectorSegment selector = getFirstSegment("my/resource/type#some-name[key=value]");
		Assert.assertEquals(Arrays.asList(pp("key", "value")), selector.getAttributes());
		Assert.assertEquals("my/resource/type", selector.getResourceType());
		Assert.assertEquals("some-name", selector.getResourceName());
	}

	@Test
	public void parseResourceTypeAndProperties() {
		SelectorSegment selector = getFirstSegment("my/resource/type[key=value][key2=value2]");
		Assert.assertEquals(Arrays.asList(pp("key", "value"), pp("key2", "value2")), selector.getAttributes());
		Assert.assertEquals("my/resource/type", selector.getResourceType());
	}

	@Test
	public void parseResourceTypeAndNameAndProperties() {
		SelectorSegment selector = getFirstSegment("my/resource/type#some-name[key=value][key2=value2]");
		Assert.assertEquals(Arrays.asList(pp("key", "value"), pp("key2", "value2")), selector.getAttributes());
		Assert.assertEquals("my/resource/type", selector.getResourceType());
		Assert.assertEquals("some-name", selector.getResourceName());
	}

	@Test
	public void parseFunction() {
		SelectorSegment selector = getFirstSegment(":eq(12)");
		Assert.assertEquals(Arrays.asList(f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseNameAndFunction() {
		SelectorSegment selector = getFirstSegment("#some-name:eq(12)");
		Assert.assertEquals("some-name", selector.getResourceName());
		Assert.assertEquals(Arrays.asList(f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseEscapedNameAndFunction() {
		SelectorSegment selector = getFirstSegment("#'jcr:content':eq(12)");
		Assert.assertEquals("jcr:content", selector.getResourceName());
		Assert.assertEquals(Arrays.asList(f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseFunctionWithFilter() {
		SelectorSegment selector = getFirstSegment(":has([key=value])");
		Assert.assertEquals(Arrays.asList(f("has", "[key=value]")), selector.getFunctions());
	}

	@Test
	public void parseNameAndFunctionWithFilter() {
		SelectorSegment selector = getFirstSegment("#some-name:has([key=value])");
		Assert.assertEquals(Arrays.asList(f("has", "[key=value]")), selector.getFunctions());
		Assert.assertEquals("some-name", selector.getResourceName());
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
		Assert.assertEquals("cq:Page", selector.getResourceType());
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parsePrimaryTypeAndFunctions() {
		SelectorSegment selector = getFirstSegment("cq:Page:first:eq(12)");
		Assert.assertEquals("cq:Page", selector.getResourceType());
		Assert.assertEquals(Arrays.asList(f("first", null), f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndFunction() {
		SelectorSegment selector = getFirstSegment("my/resource/type:first");
		Assert.assertEquals("my/resource/type", selector.getResourceType());
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndNameAndFunction() {
		SelectorSegment selector = getFirstSegment("my/resource/type#some-name:first");
		Assert.assertEquals("my/resource/type", selector.getResourceType());
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
		Assert.assertEquals("some-name", selector.getResourceName());
	}

	@Test
	public void parseResourceTypeAndFunctions() {
		SelectorSegment selector = getFirstSegment("my/resource/type:first:eq(12)");
		Assert.assertEquals("my/resource/type", selector.getResourceType());
		Assert.assertEquals(Arrays.asList(f("first", null), f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndPropertyAndFunction() {
		SelectorSegment selector = getFirstSegment("my/resource/type[key=value]:first");
		Assert.assertEquals("my/resource/type", selector.getResourceType());
		Assert.assertEquals(Arrays.asList(pp("key", "value")), selector.getAttributes());
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndNameAndPropertyAndFunction() {
		SelectorSegment selector = getFirstSegment("my/resource/type#some-name[key=value]:first");
		Assert.assertEquals(selector.getResourceType(), "my/resource/type");
		Assert.assertEquals(Arrays.asList(pp("key", "value")), selector.getAttributes());
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
		Assert.assertEquals("some-name", selector.getResourceName());
	}

	@Test
	public void parseResourceTypeAndPropertiesAndFunction() {
		SelectorSegment selector = getFirstSegment("my/resource/type[key=value][key2=value2]:first");
		Assert.assertEquals("my/resource/type", selector.getResourceType());
		Assert.assertEquals(Arrays.asList(pp("key", "value"), pp("key2", "value2")), selector.getAttributes());
		Assert.assertEquals(Arrays.asList(f("first", null)), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndPropertyAndFunctions() {
		SelectorSegment selector = getFirstSegment("my/resource/type[key=value]:first:eq(12)");
		Assert.assertEquals("my/resource/type", selector.getResourceType());
		Assert.assertEquals(Arrays.asList(pp("key", "value")), selector.getAttributes());
		Assert.assertEquals(Arrays.asList(f("first", null), f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseResourceTypeAndPropertiesAndFunctions() {
		SelectorSegment selector = getFirstSegment("my/resource/type[key=value][key2=value2]:first:eq(12)");
		Assert.assertEquals("my/resource/type", selector.getResourceType());
		Assert.assertEquals(Arrays.asList(pp("key", "value"), pp("key2", "value2")), selector.getAttributes());
		Assert.assertEquals(Arrays.asList(f("first", null), f("eq", "12")), selector.getFunctions());
	}

	@Test
	public void parseMultiSegments() {
		List<SelectorSegment> segments = SelectorParser.parse("cq:Page cq:Page", SearchStrategy.DFS)
				.getSegments();
		Assert.assertEquals(getSegments("cq:Page", " ", "cq:Page"), segments);

		segments = SelectorParser.parse("cq:Page > cq:Page", SearchStrategy.DFS).getSegments();
		Assert.assertEquals(getSegments("cq:Page", ">", "cq:Page"), segments);

		segments = SelectorParser.parse("cq:Page ~ cq:Page", SearchStrategy.DFS).getSegments();
		Assert.assertEquals(getSegments("cq:Page", "~", "cq:Page"), segments);

		segments = SelectorParser.parse("cq:Page + cq:Page", SearchStrategy.DFS).getSegments();
		Assert.assertEquals(getSegments("cq:Page", "+", "cq:Page"), segments);

		segments = SelectorParser.parse("cq:Page   cq:Page2 +  cq:Page3", SearchStrategy.DFS).getSegments();
		Assert.assertEquals(getSegments("cq:Page", " ", "cq:Page2", "+", "cq:Page3"), segments);
	}

	private static PropertyPredicate pp(String key, String value) {
		return new PropertyPredicate(key, "=", value);
	}

	private static SelectorFunction f(String functionId, String argument) {
		return new SelectorFunction(functionId, argument);
	}

	private static SelectorSegment getFirstSegment(String selector) {
		return SelectorParser.parse(selector, SearchStrategy.DFS).getSegments().get(0);
	}

	private static List<SelectorSegment> getSegments(String... segments) {
		List<SelectorSegment> list = new ArrayList<SelectorSegment>();
		if (segments.length > 0) {
			list.add(getFirstSegment(segments[0]));
		}
		for (int i = 1; i < segments.length; i += 2) {
			SelectorSegment parsed = SelectorParser.parse(segments[i + 1], SearchStrategy.DFS).getSegments()
					.get(0);
			char operator = segments[i].charAt(0);
			SelectorSegment segment = new SelectorSegment(parsed.getResourceType(), null,
					parsed.getAttributes(), parsed.getFunctions(), operator, SearchStrategy.DFS);
			list.add(segment);
		}
		return list;
	}
}