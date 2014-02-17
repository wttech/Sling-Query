package com.cognifide.sling.query.iterator;

import java.util.Iterator;
import java.util.ListIterator;

import org.apache.sling.api.resource.Resource;

import com.cognifide.sling.query.LazyList;
import com.cognifide.sling.query.api.Predicate;

public class SiblingsIterator extends AbstractIterator<Resource> {

	private final Predicate<Resource> until;

	private final ListIterator<Resource> siblings;

	private final Type type;

	private boolean finished;

	public SiblingsIterator(Predicate<Resource> until, Resource resource, Type type) {
		this.until = until;
		this.siblings = getRewindedIterator(resource, type);
		this.finished = false;
		this.type = type;
	}

	@Override
	protected Resource getElement() {
		if (finished) {
			return null;
		}
		while (type.canAdvance(siblings)) {
			Resource resource = type.advance(siblings);
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

	private static ListIterator<Resource> getRewindedIterator(Resource resource, Type type) {
		String resourceName = resource.getName();
		Resource parent = resource.getParent();
		Iterator<Resource> iterator;
		if (parent == null) {
			iterator = new ArrayIterator<Resource>(resource);
		} else {
			iterator = parent.listChildren();
		}
		ListIterator<Resource> listIterator = new LazyList<Resource>(iterator).listIterator();
		while (listIterator.hasNext()) {
			Resource sibling = listIterator.next();
			if (sibling.getName().equals(resourceName)) {
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
			public boolean canAdvance(ListIterator<Resource> iterator) {
				return iterator.hasNext();
			}

			@Override
			public Resource advance(ListIterator<Resource> iterator) {
				return iterator.next();
			}
		},
		PREV {
			@Override
			public boolean canAdvance(ListIterator<Resource> iterator) {
				return iterator.hasPrevious();
			}

			@Override
			public Resource advance(ListIterator<Resource> iterator) {
				return iterator.previous();
			}
		};

		public abstract boolean canAdvance(ListIterator<Resource> iterator);

		public abstract Resource advance(ListIterator<Resource> iterator);
	}
}
