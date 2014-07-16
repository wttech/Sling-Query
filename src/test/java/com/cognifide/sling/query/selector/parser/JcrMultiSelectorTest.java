package com.cognifide.sling.query.selector.parser;

import static com.cognifide.sling.query.selector.parser.JcrSelectorTest.parse;

import org.junit.Assert;
import org.junit.Test;

public class JcrMultiSelectorTest {
	@Test
	public void typeHierarchy() {
		String selector = "cq:Page, cq:Type";
		String jcrQuery = "SELECT * FROM [cq:Page] AS s";
		Assert.assertEquals(jcrQuery, parse(selector, "/"));

		selector = "cq:Type, cq:Page";
		Assert.assertEquals(jcrQuery, parse(selector, "/"));
	}

	@Test
	public void incompatibleTypes() {
		final String selector = "jcr:someType, cq:Type";
		final String jcrQuery = "SELECT * FROM [nt:base] AS s";
		Assert.assertEquals(jcrQuery, parse(selector, "/"));
	}

	@Test
	public void attributes() {
		final String selector = "[x=y][y=z], [a=b][c=d]";
		final String jcrQuery = "SELECT * FROM [nt:base] AS s WHERE ((s.[x] = 'y' AND s.[y] = 'z') OR (s.[a] = 'b' AND s.[c] = 'd'))";
		Assert.assertEquals(jcrQuery, parse(selector, "/"));
	}

	@Test
	public void attributesWithPath() {
		final String selector = "[x=y][y=z], [a=b][c=d]";
		final String jcrQuery = "SELECT * FROM [nt:base] AS s WHERE (ISDESCENDANTNODE(['/content']) AND ((s.[x] = 'y' AND s.[y] = 'z') OR (s.[a] = 'b' AND s.[c] = 'd')))";
		Assert.assertEquals(jcrQuery, parse(selector, "/content"));
	}
}
