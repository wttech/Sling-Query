package com.cognifide.sling.query.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.adapter.Adaptable;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.sling.query.FunctionWithSelector;
import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.TreeProvider;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.ResourceToIteratorFunction;
import com.cognifide.sling.query.api.function.ResourceToResourceFunction;
import com.cognifide.sling.query.function.ChildrenFunction;
import com.cognifide.sling.query.function.ClosestFunction;
import com.cognifide.sling.query.function.FilterFunction;
import com.cognifide.sling.query.function.HasFunction;
import com.cognifide.sling.query.function.FindFunction;
import com.cognifide.sling.query.function.LastFunction;
import com.cognifide.sling.query.function.NextFunction;
import com.cognifide.sling.query.function.NotFunction;
import com.cognifide.sling.query.function.ParentFunction;
import com.cognifide.sling.query.function.ParentsFunction;
import com.cognifide.sling.query.function.PrevFunction;
import com.cognifide.sling.query.function.SiblingsFunction;
import com.cognifide.sling.query.function.SliceFunction;
import com.cognifide.sling.query.iterator.AdaptToIterator;
import com.cognifide.sling.query.predicate.RejectingPredicate;
import com.cognifide.sling.query.resource.ResourceTreeProvider;
import com.cognifide.sling.query.selector.SelectorFunction;

/**
 * SlingQuery is a Sling resource tree traversal tool inspired by the jQuery. Full documentation could be
 * found on the <a href="https://github.com/Cognifide/Sling-Query">Github project page</a>.
 * 
 * @author Tomasz Rękawek
 * 
 */
public class SlingQuery<T> implements Iterable<T> {
	private final List<IteratorToIteratorFunction<T>> functions = new ArrayList<IteratorToIteratorFunction<T>>();

	private final List<T> resources;

	private final SearchStrategy searchStrategy;

	private final TreeProvider<T> provider;

	/**
	 * Create a new SlingQuery object, using passed resources as an initial collection.
	 * 
	 * @param resources Initial collection
	 * @return New SlingQuery object.
	 */
	public static SlingQuery<Resource> $(Resource... resources) {
		return new SlingQuery<Resource>(new ResourceTreeProvider(), resources);
	}

	/**
	 * Create a new SlingQuery object, using repository root / as an initial collection.
	 * 
	 * @param resourceResolver Sling resource resolver
	 * @return New SlingQuery object.
	 */
	public static SlingQuery<Resource> $(ResourceResolver resolver) {
		return $(resolver.getResource("/"));
	}

	private SlingQuery(TreeProvider<T> provider, T[] resources) {
		this.provider = provider;
		this.resources = Arrays.asList(resources);
		this.searchStrategy = SearchStrategy.DFS;
	}

	private SlingQuery(SlingQuery<T> original) {
		this(original, original.searchStrategy);
	}

	private SlingQuery(SlingQuery<T> original, SearchStrategy searchStrategy) {
		this.functions.addAll(original.functions);
		this.resources = new ArrayList<T>(original.resources);
		this.searchStrategy = searchStrategy;
		this.provider = original.provider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		Iterator<T> iterator = resources.iterator();
		for (IteratorToIteratorFunction<T> operation : functions) {
			iterator = operation.apply(iterator);
		}
		return iterator;
	}

	/**
	 * Transform SlingQuery collection into a lazy list.
	 * 
	 * @return List containing all elements from the collection.
	 */
	public List<T> asList() {
		return new LazyList<T>(iterator());
	}

	/**
	 * Get list of the children for each resource in the collection.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> children() {
		return children("");
	}

	/**
	 * Get list of the children for each resource in the collection.
	 * 
	 * @param selector Children filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> children(String selector) {
		return functionWithSelector(new ChildrenFunction<T>(provider), selector);
	}

	/**
	 * For each resource in the collection, return the first element matching the selector testing the
	 * resource itself and traversing up its ancestors.
	 * 
	 * @param selector Ancestor filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> closest(String selector) {
		return functionWithSelector(new ClosestFunction<T>(parse(selector), provider), "");
	}

	/**
	 * Reduce resource collection to the one resource at the given 0-based index.
	 * 
	 * @param index 0-based index
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> eq(int index) {
		return slice(index, index);
	}

	/**
	 * Filter resource collection using given predicate object.
	 * 
	 * @param predicate Collection filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> filter(Predicate<T> predicate) {
		return function(new FilterFunction<T>(predicate));
	}

	/**
	 * For each resource in collection use depth-first search to return all its descendants. Please notice
	 * that invoking this method on a resource being a root of a large subtree may and will cause performance
	 * problems.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> find() {
		return find("");
	}

	/**
	 * For each resource in collection use breadth-first search to return all its descendants. Please notice
	 * that invoking this method on a resource being a root of a large subtree may and will cause performance
	 * problems.
	 * 
	 * @param selector descendants filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> find(String selector) {
		return functionWithSelector(new FindFunction<T>(selector, searchStrategy, provider), selector);
	}

	/**
	 * Filter resource collection to the first element. Equivalent to {@code eq(0)} or {@code slice(0, 0)}.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> first() {
		return eq(0);
	}

	/**
	 * This method allows to process resource collection using custom function and then filter the results by
	 * a selector string.
	 * 
	 * @param function Object implementing one of the interfaces: {@link ResourceToResourceFunction},
	 * {@link ResourceToIteratorFunction} or {@link IteratorToIteratorFunction}
	 * @param selector Result filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> functionWithSelector(Function<?, ?> function, String selector) {
		return function(new FunctionWithSelector<T>(function, selector, searchStrategy, provider));
	}

	/**
	 * Pick such resources from the collection that have descendant matching the selector.
	 * 
	 * @param selector Descendant selector
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> has(String selector) {
		return functionWithSelector(new HasFunction<T>(selector, searchStrategy, provider), "");
	}

	/**
	 * Filter resource collection to the last element.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> last() {
		return functionWithSelector(new LastFunction<T>(), "");
	}

	/**
	 * Transform the whole collection to a new {@link Iterable} object, invoking
	 * {@link Adaptable#adaptTo(Class)} method on each resource. If some resource can't be adapted to the
	 * class (eg. {@code adaptTo()} returns {@code null}), it will be skipped.
	 * 
	 * @param clazz Class used to adapt the resources
	 * @return new iterable containing succesfully adapted resources
	 */
	public <E> Iterable<E> map(final Class<? extends E> clazz) {
		return new Iterable<E>() {
			@Override
			public Iterator<E> iterator() {
				return new AdaptToIterator<T, E>(SlingQuery.this.iterator(), clazz);
			}
		};
	}

	/**
	 * Return the next sibling for each resource in the collection.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> next() {
		return next("");
	}

	/**
	 * Return the next sibling for each resource in the collection and filter it by a selector. If the next
	 * sibling doesn't match it, empty collection will be returned.
	 * 
	 * @param selector Next sibling filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> next(String selector) {
		return functionWithSelector(new NextFunction<T>(null, provider), selector);
	}

	/**
	 * Return all following siblings for each resource in the collection.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> nextAll() {
		return nextAll("");
	}

	/**
	 * Return all following siblings for each resource in the collection, filtering them by a selector.
	 * 
	 * @param selector Following siblings filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> nextAll(String selector) {
		return functionWithSelector(new NextFunction<T>(new RejectingPredicate<T>(), provider), selector);
	}

	/**
	 * Return all following siblings for each resource in the collection up to, but not including, resource
	 * matched by a selector.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> nextUntil(String until) {
		return nextUntil(until, "");
	}

	/**
	 * Return all following siblings for each resource in the collection up to, but not including, resource
	 * matched by a selector.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @param selector Following siblings filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> nextUntil(String until, String selector) {
		return functionWithSelector(new NextFunction<T>(parse(until), provider), selector);
	}

	/**
	 * Remove elements from the collection.
	 * 
	 * @param selector Selector used to remove resources
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> not(String selector) {
		return functionWithSelector(new NotFunction<T>(parse(selector)), "");
	}

	/**
	 * Replace each element in the collection with its parent.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> parent() {
		return functionWithSelector(new ParentFunction<T>(provider), "");
	}

	/**
	 * For each element in the collection find its all ancestor.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> parents() {
		return parents("");
	}

	/**
	 * For each element in the collection find its all ancestor, filtered by a selector.
	 * 
	 * @param selector Parents filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> parents(String selector) {
		return functionWithSelector(new ParentsFunction<T>(new RejectingPredicate<T>(), provider), selector);
	}

	/**
	 * For each element in the collection find all of its ancestors until the predicate is met.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> parentsUntil(String until) {
		return functionWithSelector(new ParentsFunction<T>(parse(until), provider), "");
	}

	/**
	 * For each element in the collection find all of its ancestors until the predicate is met, optionally
	 * filter by a selector.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @param selector Parents filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> parentsUntil(String until, String selector) {
		return functionWithSelector(new ParentsFunction<T>(parse(until), provider), selector);
	}

	/**
	 * Return the previous sibling for each resource in the collection.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> prev() {
		return prev("");
	}

	/**
	 * Return the previous sibling for each resource in the collection and filter it by a selector. If the
	 * previous sibling doesn't match it, empty collection will be returned.
	 * 
	 * @param selector Previous sibling filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> prev(String selector) {
		return functionWithSelector(new PrevFunction<T>(null, provider), selector);
	}

	/**
	 * Return all previous siblings for each resource in the collection.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> prevAll() {
		return prevAll("");
	}

	/**
	 * Return all previous siblings for each resource in the collection, filtering them by a selector.
	 * 
	 * @param selector Previous siblings filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> prevAll(String selector) {
		return functionWithSelector(new PrevFunction<T>(new RejectingPredicate<T>(), provider), selector);
	}

	/**
	 * Return all previous siblings for each resource in the collection up to, but not including, resource
	 * matched by a selector.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> prevUntil(String until) {
		return prevUntil(until, "");
	}

	/**
	 * Return all previous siblings for each resource in the collection up to, but not including, resource
	 * matched by a selector.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @param selector Previous siblings filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> prevUntil(String until, String selector) {
		return functionWithSelector(new PrevFunction<T>(parse(until), provider), selector);
	}

	/**
	 * Set new search strategy, which will be used in {@link SlingQuery#find()} and
	 * {@link SlingQuery#has(String)} functions.
	 * 
	 * @param strategy Search strategy type
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> searchStrategy(SearchStrategy strategy) {
		return new SlingQuery<T>(this, strategy);
	}

	/**
	 * Return siblings for the given resources.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> siblings() {
		return siblings("");
	}

	/**
	 * Return siblings for the given resources filtered by a selector.
	 * 
	 * @param selector Siblings filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> siblings(String selector) {
		return functionWithSelector(new SiblingsFunction<T>(provider), selector);
	}

	/**
	 * Filter out first {@code from} resources from the collection.
	 * 
	 * @param from How many resources to cut out
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> slice(int from) {
		if (from < 0) {
			throw new IndexOutOfBoundsException();
		}
		return function(new SliceFunction<T>(from));
	}

	/**
	 * Reduce the collection to a subcollection specified by a given range. Both from and to are inclusive,
	 * 0-based indices.
	 * 
	 * @param from Low endpoint (inclusive) of the subcollection
	 * @param to High endpoint (inclusive) of the subcollection
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery<T> slice(int from, int to) {
		if (from < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (from > to) {
			throw new IllegalArgumentException();
		}
		return function(new SliceFunction<T>(from, to));
	}

	private SlingQuery<T> function(IteratorToIteratorFunction<T> function) {
		SlingQuery<T> newQuery = new SlingQuery<T>(this);
		newQuery.functions.add(function);
		return newQuery;
	}

	private SelectorFunction<T> parse(String selector) {
		return SelectorFunction.parse(selector, searchStrategy, provider);
	}
}