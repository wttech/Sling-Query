package com.cognifide.sling.query.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.adapter.Adaptable;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.api.function.OptionIteratorToIteratorFunction;
import com.cognifide.sling.query.api.function.ElementToIteratorFunction;
import com.cognifide.sling.query.api.function.ElementToElementFunction;
import com.cognifide.sling.query.function.ChildrenFunction;
import com.cognifide.sling.query.function.ClosestFunction;
import com.cognifide.sling.query.function.FilterFunction;
import com.cognifide.sling.query.function.FunctionWithSelector;
import com.cognifide.sling.query.function.HasFunction;
import com.cognifide.sling.query.function.FindFunction;
import com.cognifide.sling.query.function.IdentityFunction;
import com.cognifide.sling.query.function.LastFunction;
import com.cognifide.sling.query.function.NextFunction;
import com.cognifide.sling.query.function.NotFunction;
import com.cognifide.sling.query.function.OptionCompositeFunction;
import com.cognifide.sling.query.function.ParentFunction;
import com.cognifide.sling.query.function.ParentsFunction;
import com.cognifide.sling.query.function.PrevFunction;
import com.cognifide.sling.query.function.SiblingsFunction;
import com.cognifide.sling.query.function.SliceFunction;
import com.cognifide.sling.query.iterator.AdaptToIterator;
import com.cognifide.sling.query.iterator.EmptyElementFilter;
import com.cognifide.sling.query.predicate.RejectingPredicate;
import com.cognifide.sling.query.resource.ResourceTreeProvider;
import com.cognifide.sling.query.selector.Option;
import com.cognifide.sling.query.selector.SelectorFunction;

/**
 * SlingQuery is a Sling resource tree traversal tool inspired by the jQuery. Full documentation could be
 * found on the <a href="https://github.com/Cognifide/Sling-Query">Github project page</a>.
 * 
 * @author Tomasz Rękawek
 * 
 */
public class SlingQuery implements Iterable<Resource> {
	private final List<Function<?, ?>> functions = new ArrayList<Function<?, ?>>();

	private final List<Option<Resource>> resources;

	private final SearchStrategy searchStrategy;

	private final TreeProvider<Resource> provider;

	/**
	 * Create a new SlingQuery object, using passed resources as an initial collection.
	 * 
	 * @param resources Initial collection
	 * @return New SlingQuery object.
	 */
	public static SlingQuery $(Resource... resources) {
		return new SlingQuery(new ResourceTreeProvider(), resources);
	}

	/**
	 * Create a new SlingQuery object, using repository root / as an initial collection.
	 * 
	 * @param resourceResolver Sling resource resolver
	 * @return New SlingQuery object.
	 */
	public static SlingQuery $(ResourceResolver resolver) {
		return $(resolver.getResource("/"));
	}

	private SlingQuery(TreeProvider<Resource> provider, Resource[] resources) {
		this.provider = provider;
		this.resources = new ArrayList<Option<Resource>>();
		for (Resource r : resources) {
			this.resources.add(new Option<Resource>(r));
		}
		this.searchStrategy = SearchStrategy.DFS;
	}

	private SlingQuery(SlingQuery original) {
		this(original, original.searchStrategy);
	}

	private SlingQuery(SlingQuery original, SearchStrategy searchStrategy) {
		this.functions.addAll(original.functions);
		this.resources = new ArrayList<Option<Resource>>(original.resources);
		this.searchStrategy = searchStrategy;
		this.provider = original.provider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Resource> iterator() {
		OptionIteratorToIteratorFunction<Resource> f = new OptionCompositeFunction<Resource>(functions);
		Iterator<Option<Resource>> iterator = f.apply(resources.iterator());
		return new EmptyElementFilter<Resource>(iterator);
	}

	/**
	 * Transform SlingQuery collection into a lazy list.
	 * 
	 * @return List containing all elements from the collection.
	 */
	public List<Resource> asList() {
		return new LazyList<Resource>(iterator());
	}

	/**
	 * Get list of the children for each resource in the collection.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery children() {
		return children("");
	}

	/**
	 * Get list of the children for each resource in the collection.
	 * 
	 * @param selector Children filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery children(String selector) {
		return functionWithSelector(new ChildrenFunction<Resource>(provider), selector);
	}

	/**
	 * For each resource in the collection, return the first element matching the selector testing the
	 * resource itself and traversing up its ancestors.
	 * 
	 * @param selector Ancestor filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery closest(String selector) {
		return functionWithSelector(new ClosestFunction<Resource>(parse(selector), provider), "");
	}

	/**
	 * Reduce resource collection to the one resource at the given 0-based index.
	 * 
	 * @param index 0-based index
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery eq(int index) {
		return slice(index, index);
	}

	/**
	 * Filter resource collection using given predicate object.
	 * 
	 * @param predicate Collection filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery filter(Predicate<Resource> predicate) {
		return function(new FilterFunction<Resource>(predicate));
	}

	/**
	 * Filter resource collection using given selector.
	 * 
	 * @param selector Selector
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery filter(String selector) {
		return functionWithSelector(new IdentityFunction<Resource>(), selector);
	}

	/**
	 * For each resource in collection use depth-first search to return all its descendants. Please notice
	 * that invoking this method on a resource being a root of a large subtree may and will cause performance
	 * problems.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery find() {
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
	public SlingQuery find(String selector) {
		return functionWithSelector(new FindFunction<Resource>(selector, searchStrategy, provider), selector);
	}

	/**
	 * Filter resource collection to the first element. Equivalent to {@code eq(0)} or {@code slice(0, 0)}.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery first() {
		return eq(0);
	}

	/**
	 * This method allows to process resource collection using custom function and then filter the results by
	 * a selector string.
	 * 
	 * @param function Object implementing one of the interfaces: {@link ElementToElementFunction},
	 * {@link ElementToIteratorFunction} or {@link OptionIteratorToIteratorFunction}
	 * @param selector Result filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery functionWithSelector(Function<?, ?> function, String selector) {
		return function(new FunctionWithSelector<Resource>(function, selector, searchStrategy, provider));
	}

	/**
	 * Pick such resources from the collection that have descendant matching the selector.
	 * 
	 * @param selector Descendant selector
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery has(String selector) {
		return functionWithSelector(new HasFunction<Resource>(selector, searchStrategy, provider), "");
	}

	/**
	 * Filter resource collection to the last element.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery last() {
		return functionWithSelector(new LastFunction<Resource>(), "");
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
				return new AdaptToIterator<Resource, E>(SlingQuery.this.iterator(), clazz);
			}
		};
	}

	/**
	 * Return the next sibling for each resource in the collection.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery next() {
		return next("");
	}

	/**
	 * Return the next sibling for each resource in the collection and filter it by a selector. If the next
	 * sibling doesn't match it, empty collection will be returned.
	 * 
	 * @param selector Next sibling filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery next(String selector) {
		return functionWithSelector(new NextFunction<Resource>(null, provider), selector);
	}

	/**
	 * Return all following siblings for each resource in the collection.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery nextAll() {
		return nextAll("");
	}

	/**
	 * Return all following siblings for each resource in the collection, filtering them by a selector.
	 * 
	 * @param selector Following siblings filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery nextAll(String selector) {
		return functionWithSelector(new NextFunction<Resource>(new RejectingPredicate<Resource>(), provider),
				selector);
	}

	/**
	 * Return all following siblings for each resource in the collection up to, but not including, resource
	 * matched by a selector.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery nextUntil(String until) {
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
	public SlingQuery nextUntil(String until, String selector) {
		return functionWithSelector(new NextFunction<Resource>(parse(until), provider), selector);
	}

	/**
	 * Remove elements from the collection.
	 * 
	 * @param selector Selector used to remove resources
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery not(String selector) {
		return function(new NotFunction<Resource>(parse(selector)));
	}

	/**
	 * Replace each element in the collection with its parent.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery parent() {
		return functionWithSelector(new ParentFunction<Resource>(provider), "");
	}

	/**
	 * For each element in the collection find its all ancestor.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery parents() {
		return parents("");
	}

	/**
	 * For each element in the collection find its all ancestor, filtered by a selector.
	 * 
	 * @param selector Parents filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery parents(String selector) {
		return functionWithSelector(new ParentsFunction<Resource>(new RejectingPredicate<Resource>(),
				provider), selector);
	}

	/**
	 * For each element in the collection find all of its ancestors until the predicate is met.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery parentsUntil(String until) {
		return functionWithSelector(new ParentsFunction<Resource>(parse(until), provider), "");
	}

	/**
	 * For each element in the collection find all of its ancestors until the predicate is met, optionally
	 * filter by a selector.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @param selector Parents filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery parentsUntil(String until, String selector) {
		return functionWithSelector(new ParentsFunction<Resource>(parse(until), provider), selector);
	}

	/**
	 * Return the previous sibling for each resource in the collection.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery prev() {
		return prev("");
	}

	/**
	 * Return the previous sibling for each resource in the collection and filter it by a selector. If the
	 * previous sibling doesn't match it, empty collection will be returned.
	 * 
	 * @param selector Previous sibling filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery prev(String selector) {
		return functionWithSelector(new PrevFunction<Resource>(null, provider), selector);
	}

	/**
	 * Return all previous siblings for each resource in the collection.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery prevAll() {
		return prevAll("");
	}

	/**
	 * Return all previous siblings for each resource in the collection, filtering them by a selector.
	 * 
	 * @param selector Previous siblings filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery prevAll(String selector) {
		return functionWithSelector(new PrevFunction<Resource>(new RejectingPredicate<Resource>(), provider),
				selector);
	}

	/**
	 * Return all previous siblings for each resource in the collection up to, but not including, resource
	 * matched by a selector.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery prevUntil(String until) {
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
	public SlingQuery prevUntil(String until, String selector) {
		return functionWithSelector(new PrevFunction<Resource>(parse(until), provider), selector);
	}

	/**
	 * Set new search strategy, which will be used in {@link SlingQuery#find()} and
	 * {@link SlingQuery#has(String)} functions.
	 * 
	 * @param strategy Search strategy type
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery searchStrategy(SearchStrategy strategy) {
		return new SlingQuery(this, strategy);
	}

	/**
	 * Return siblings for the given resources.
	 * 
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery siblings() {
		return siblings("");
	}

	/**
	 * Return siblings for the given resources filtered by a selector.
	 * 
	 * @param selector Siblings filter
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery siblings(String selector) {
		return functionWithSelector(new SiblingsFunction<Resource>(provider), selector);
	}

	/**
	 * Filter out first {@code from} resources from the collection.
	 * 
	 * @param from How many resources to cut out
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery slice(int from) {
		if (from < 0) {
			throw new IndexOutOfBoundsException();
		}
		return function(new SliceFunction<Resource>(from));
	}

	/**
	 * Reduce the collection to a subcollection specified by a given range. Both from and to are inclusive,
	 * 0-based indices.
	 * 
	 * @param from Low endpoint (inclusive) of the subcollection
	 * @param to High endpoint (inclusive) of the subcollection
	 * @return a {@link SlingQuery} object transformed by this operation
	 */
	public SlingQuery slice(int from, int to) {
		if (from < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (from > to) {
			throw new IllegalArgumentException();
		}
		return function(new SliceFunction<Resource>(from, to));
	}

	private SlingQuery function(OptionIteratorToIteratorFunction<Resource> function) {
		SlingQuery newQuery = new SlingQuery(this);
		newQuery.functions.add(function);
		return newQuery;
	}

	private SelectorFunction<Resource> parse(String selector) {
		return SelectorFunction.parse(selector, searchStrategy, provider);
	}
}