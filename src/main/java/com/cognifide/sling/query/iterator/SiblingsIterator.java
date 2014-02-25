package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.ListIterator;

import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.api.Predicate;
import com.cognifide.sling.query.api.TreeProvider;

public class SiblingsIterator<T> extends AbstractIterator<T> {

	private final Predicate<T> until;

	private final ListIterator<T> siblings;

	private final Type type;

	private final TreeProvider<T> provider;

	private boolean finished;

	public SiblingsIterator(Predicate<T> until, T resource, Type type, TreeProvider<T> provider) {
		this.provider = provider;
		this.until = until;
		this.siblings = getRewindedIterator(resource, type);
		this.finished = false;
		this.type = type;
	}

	@Override
	protected T getElement() {
		if (finished) {
			return null;
		}
		while (type.canAdvance(siblings)) {
			T resource = type.advance(siblings);
			if (until != null && until.accepts(resource)) {
				finished = true;
				return null;
			}
			if (until == null) {
				finished = true;
			}
			return resource;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private ListIterator<T> getRewindedIterator(T resource, Type type) {
		String resourceName = provider.getName(resource);
		T parent = provider.getParent(resource);
		Iterator<T> iterator;
		if (parent == null) {
			iterator = new ArrayIterator<T>(resource);
		} else {
			iterator = provider.listChildren(parent);
		}
		ListIterator<T> listIterator = new LazyList<T>(iterator).listIterator();
		while (listIterator.hasNext()) {
			T sibling = listIterator.next();
			if (provider.getName(sibling).equals(resourceName)) {
				break;
			}
		}
		if (type == Type.PREV) {
			listIterator.previous();
		}
		return listIterator;
	}

	public enum Type {
		NEXT {
			@Override
			public boolean canAdvance(ListIterator<?> iterator) {
				return iterator.hasNext();
			}

			@Override
			public <T> T advance(ListIterator<T> iterator) {
				return iterator.next();
			}
		},
		PREV {
			@Override
			public boolean canAdvance(ListIterator<?> iterator) {
				return iterator.hasPrevious();
			}

			@Override
			public <T> T advance(ListIterator<T> iterator) {
				return iterator.previous();
			}
		};

		public abstract boolean canAdvance(ListIterator<?> iterator);

		public abstract <T> T advance(ListIterator<T> iterator);
	}
}
