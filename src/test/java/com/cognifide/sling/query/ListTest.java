package com.cognifide.sling.query;

import static com.cognifide.sling.query.TestUtils.assertResourceSetEquals;
import static com.cognifide.sling.query.api.SlingQuery.$;

import java.util.List;
import java.util.ListIterator;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Test;

public class ListTest {

	private Resource tree = TestUtils.getTree();

	@Test
	public void testIterator() {
		List<Resource> list = $(tree).children().asList();
		assertResourceSetEquals(list.iterator(), "jcr:content", "application", "home");
	}
	
	@Test
	public void testListIterator() {
		ListIterator<Resource> iterator = $(tree).children().asList().listIterator();
		Assert.assertEquals("jcr:content", iterator.next().getName());
		Assert.assertEquals("application", iterator.next().getName());
		Assert.assertEquals("home", iterator.next().getName());
		Assert.assertEquals("home", iterator.previous().getName());
		Assert.assertEquals("application", iterator.previous().getName());
		Assert.assertEquals("jcr:content", iterator.previous().getName());
	}

	@Test
	public void testSize() {
		List<Resource> list = $(tree).children().asList();
		Assert.assertEquals(3, list.size());
	}

	@Test
	public void testGet() {
		List<Resource> list = $(tree).children().asList();
		Assert.assertEquals("jcr:content", list.get(0).getName());
		Assert.assertEquals("application", list.get(1).getName());
		Assert.assertEquals("home", list.get(2).getName());
	}

	@Test
	public void testIndexOf() {
		Resource home = $(tree).children("#home").iterator().next();
		List<Resource> list = $(tree).children().asList();
		Assert.assertEquals(2, list.indexOf(home));
		Assert.assertEquals(-1, list.indexOf(tree));
	}

	@Test
	public void testContains() {
		Resource home = $(tree).children("#home").iterator().next();
		List<Resource> list = $(tree).children().asList();
		Assert.assertEquals(true, list.contains(home));
		Assert.assertEquals(false, list.contains(tree));
	}

	@Test
	public void testEmpty() {
		Assert.assertEquals(true, $(tree).children("#aaa").asList().isEmpty());
		Assert.assertEquals(false, $(tree).children().asList().isEmpty());
	}

	@Test
	public void testSublist() {
		List<Resource> list = $(tree).children().asList().subList(1, 3);
		assertResourceSetEquals(list.iterator(), "application", "home");
	}
}
