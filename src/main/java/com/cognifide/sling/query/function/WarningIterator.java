package com.cognifide.sling.query.function;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.sling.query.iterator.AbstractIterator;

public class WarningIterator<T> extends AbstractIterator<T> {

	private static final Logger LOG = LoggerFactory.getLogger("SlingQuery");

	private static final int DEFAULT_LIMIT = 100;

	private final Iterator<T> iterator;

	private final int limit;

	private int count = 0;

	public WarningIterator(Iterator<T> iterator) {
		this(iterator, DEFAULT_LIMIT);
	}

	public WarningIterator(Iterator<T> iterator, int limit) {
		this.iterator = iterator;
		this.limit = limit;
	}

	@Override
	protected T getElement() {
		if (!iterator.hasNext()) {
			return null;
		}
		if (count++ == limit) {
			LOG.warn(
					"Number of processed resources exceeded {}. Consider using a JCR query instead of SlingQuery. More info here: http://git.io/h2HeUQ",
					new Object[] { limit });
		}
		return iterator.next();
	}

}
