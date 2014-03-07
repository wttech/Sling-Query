package com.cognifide.sling.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.cognifide.sling.query.api.Function;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.SearchStrategy;
import com.cognifide.sling.query.api.TreeProvider;
import com.cognifide.sling.query.api.function.Option;
import com.cognifide.sling.query.api.function.IteratorToIteratorFunction;
import com.cognifide.sling.query.function.ChildrenFunction;
import com.cognifide.sling.query.function.ClosestFunction;
import com.cognifide.sling.query.function.FilterFunction;
import com.cognifide.sling.query.function.FilteredFunction;
import com.cognifide.sling.query.function.HasFunction;
import com.cognifide.sling.query.function.FindFunction;
import com.cognifide.sling.query.function.IdentityFunction;
import com.cognifide.sling.query.function.LastFunction;
import com.cognifide.sling.query.function.NextFunction;
import com.cognifide.sling.query.function.NotFunction;
import com.cognifide.sling.query.function.CompositeFunction;
import com.cognifide.sling.query.function.ParentFunction;
import com.cognifide.sling.query.function.ParentsFunction;
import com.cognifide.sling.query.function.PrevFunction;
import com.cognifide.sling.query.function.SiblingsFunction;
import com.cognifide.sling.query.function.SliceFunction;
import com.cognifide.sling.query.iterator.EmptyElementFilter;
import com.cognifide.sling.query.iterator.OptionDecoratingIterator;
import com.cognifide.sling.query.iterator.OptionStrippingIterator;
import com.cognifide.sling.query.predicate.RejectingPredicate;
import com.cognifide.sling.query.selector.SelectorFunction;

public abstract class JavaQuery<T, Q extends JavaQuery<T, Q>> implements Iterable<T> {

	protected final List<Function<?, ?>> functions = new ArrayList<Function<?, ?>>();

	private final List<T> Ts;

	private final SearchStrategy searchStrategy;

	private final TreeProvider<T> provider;

	protected JavaQuery(TreeProvider<T> provider, T[] initialTs) {
		this.provider = provider;
		this.Ts = new ArrayList<T>(Arrays.asList(initialTs));
		this.searchStrategy = SearchStrategy.DFS;
	}

	protected JavaQuery(JavaQuery<T, Q> original, SearchStrategy searchStrategy) {
		this.functions.addAll(original.functions);
		this.Ts = new ArrayList<T>(original.Ts);
		this.searchStrategy = searchStrategy;
		this.provider = original.provider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		IteratorToIteratorFunction<T> f = new CompositeFunction<T>(functions);
		Iterator<Option<T>> iterator = f.apply(new OptionDecoratingIterator<T>(Ts.iterator()));
		iterator = new EmptyElementFilter<T>(iterator);
		return new OptionStrippingIterator<T>(iterator);
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
	 * Get list of the children for each Resource in the collection.
	 * 
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q children() {
		return function(new ChildrenFunction<T>(provider));
	}

	/**
	 * Get list of the children for each Resource in the collection.
	 * 
	 * @param selector Children filter
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q children(String selector) {
		return functionWithSelector(new ChildrenFunction<T>(provider), selector);
	}

	/**
	 * For each Resource in the collection, return the first element matching the selector testing the
	 * Resource itself and traversing up its ancestors.
	 * 
	 * @param selector Ancestor filter
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q closest(String selector) {
		return function(new ClosestFunction<T>(parse(selector), provider));
	}

	/**
	 * Reduce Resource collection to the one Resource at the given 0-based index.
	 * 
	 * @param index 0-based index
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q eq(int index) {
		return slice(index, index);
	}

	/**
	 * Filter Resource collection using given predicate object.
	 * 
	 * @param predicate Collection filter
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q filter(Predicate<T> predicate) {
		return function(new FilterFunction<T>(predicate));
	}

	/**
	 * Filter Resource collection using given selector.
	 * 
	 * @param selector Selector
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q filter(String selector) {
		return functionWithSelector(new IdentityFunction<T>(), selector);
	}

	/**
	 * For each Resource in collection use depth-first search to return all its descendants. Please notice
	 * that invoking this method on a Resource being a root of a large subtree may and will cause performance
	 * problems.
	 * 
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q find() {
		return function(new FindFunction<T>(searchStrategy, provider, ""));
	}

	/**
	 * For each Resource in collection use breadth-first search to return all its descendants. Please notice
	 * that invoking this method on a Resource being a root of a large subtree may and will cause performance
	 * problems.
	 * 
	 * @param selector descendants filter
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q find(String selector) {
		return functionWithSelector(new FindFunction<T>(searchStrategy, provider, selector), selector);
	}

	/**
	 * Filter Resource collection to the first element. Equivalent to {@code eq(0)} or {@code slice(0, 0)}.
	 * 
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q first() {
		return eq(0);
	}

	/**
	 * Pick such Resources from the collection that have descendant matching the selector.
	 * 
	 * @param selector Descendant selector
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q has(String selector) {
		return function(new HasFunction<T>(selector, searchStrategy, provider));
	}

	/**
	 * Filter Resource collection to the last element.
	 * 
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q last() {
		return function(new LastFunction<T>());
	}

	/**
	 * Return the next sibling for each Resource in the collection.
	 * 
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q next() {
		return function(new NextFunction<T>(null, provider));
	}

	/**
	 * Return the next sibling for each Resource in the collection and filter it by a selector. If the next
	 * sibling doesn't match it, empty collection will be returned.
	 * 
	 * @param selector Next sibling filter
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q next(String selector) {
		return functionWithSelector(new NextFunction<T>(null, provider), selector);
	}

	/**
	 * Return all following siblings for each Resource in the collection.
	 * 
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q nextAll() {
		return function(new NextFunction<T>(new RejectingPredicate<T>(), provider));
	}

	/**
	 * Return all following siblings for each Resource in the collection, filtering them by a selector.
	 * 
	 * @param selector Following siblings filter
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q nextAll(String selector) {
		return functionWithSelector(new NextFunction<T>(new RejectingPredicate<T>(), provider), selector);
	}

	/**
	 * Return all following siblings for each Resource in the collection up to, but not including, Resource
	 * matched by a selector.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q nextUntil(String until) {
		return function(new NextFunction<T>(parse(until), provider));
	}

	/**
	 * Return all following siblings for each Resource in the collection up to, but not including, Resource
	 * matched by a selector.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @param selector Following siblings filter
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q nextUntil(String until, String selector) {
		return functionWithSelector(new NextFunction<T>(parse(until), provider), selector);
	}

	/**
	 * Remove elements from the collection.
	 * 
	 * @param selector Selector used to remove Ts
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q not(String selector) {
		return function(new NotFunction<T>(parse(selector)));
	}

	/**
	 * Replace each element in the collection with its parent.
	 * 
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q parent() {
		return function(new ParentFunction<T>(provider));
	}

	/**
	 * For each element in the collection find its all ancestor.
	 * 
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q parents() {
		return function(new ParentsFunction<T>(new RejectingPredicate<T>(), provider));
	}

	/**
	 * For each element in the collection find its all ancestor, filtered by a selector.
	 * 
	 * @param selector Parents filter
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q parents(String selector) {
		return functionWithSelector(new ParentsFunction<T>(new RejectingPredicate<T>(), provider), selector);
	}

	/**
	 * For each element in the collection find all of its ancestors until the predicate is met.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q parentsUntil(String until) {
		return function(new ParentsFunction<T>(parse(until), provider));
	}

	/**
	 * For each element in the collection find all of its ancestors until the predicate is met, optionally
	 * filter by a selector.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @param selector Parents filter
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q parentsUntil(String until, String selector) {
		return functionWithSelector(new ParentsFunction<T>(parse(until), provider), selector);
	}

	/**
	 * Return the previous sibling for each Resource in the collection.
	 * 
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q prev() {
		return function(new PrevFunction<T>(provider));
	}

	/**
	 * Return the previous sibling for each Resource in the collection and filter it by a selector. If the
	 * previous sibling doesn't match it, empty collection will be returned.
	 * 
	 * @param selector Previous sibling filter
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q prev(String selector) {
		return functionWithSelector(new PrevFunction<T>(null, provider), selector);
	}

	/**
	 * Return all previous siblings for each Resource in the collection.
	 * 
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q prevAll() {
		return function(new PrevFunction<T>(new RejectingPredicate<T>(), provider));
	}

	/**
	 * Return all previous siblings for each Resource in the collection, filtering them by a selector.
	 * 
	 * @param selector Previous siblings filter
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q prevAll(String selector) {
		return functionWithSelector(new PrevFunction<T>(new RejectingPredicate<T>(), provider), selector);
	}

	/**
	 * Return all previous siblings for each Resource in the collection up to, but not including, Resource
	 * matched by a selector.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q prevUntil(String until) {
		return function(new PrevFunction<T>(parse(until), provider));
	}

	/**
	 * Return all previous siblings for each Resource in the collection up to, but not including, Resource
	 * matched by a selector.
	 * 
	 * @param until Selector marking when the operation should stop
	 * @param selector Previous siblings filter
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q prevUntil(String until, String selector) {
		return functionWithSelector(new PrevFunction<T>(parse(until), provider), selector);
	}

	/**
	 * Set new search strategy, which will be used in {@link Q#find()} and {@link Q#has(String)} functions.
	 * 
	 * @param strategy Search strategy type
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q searchStrategy(SearchStrategy strategy) {
		return clone(this, strategy);
	}

	/**
	 * Return siblings for the given Ts.
	 * 
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q siblings() {
		return siblings("");
	}

	/**
	 * Return siblings for the given Resources filtered by a selector.
	 * 
	 * @param selector Siblings filter
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q siblings(String selector) {
		return functionWithSelector(new SiblingsFunction<T>(provider), selector);
	}

	/**
	 * Filter out first {@code from} Resources from the collection.
	 * 
	 * @param from How many Resources to cut out
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q slice(int from) {
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
	 * @return new SlingQuery object transformed by this operation
	 */
	public Q slice(int from, int to) {
		if (from < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (from > to) {
			throw new IllegalArgumentException();
		}
		return function(new SliceFunction<T>(from, to));
	}

	private Q functionWithSelector(Function<?, ?> function, String selector) {
		return function(new FilteredFunction<T>(function, selector, searchStrategy, provider));
	}

	private Q function(Function<?, ?> function) {
		Q newQuery = clone(this, this.searchStrategy);
		newQuery.functions.add(function);
		return newQuery;
	}

	private SelectorFunction<T> parse(String selector) {
		return new SelectorFunction<T>(selector, provider, searchStrategy);
	}

	protected abstract Q clone(JavaQuery<T, Q> original, SearchStrategy strategy);
}