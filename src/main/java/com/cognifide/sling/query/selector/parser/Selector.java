package com.cognifide.sling.query.selector.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Selector {
	private final List<SelectorSegment> segments;

	public Selector() {
		this.segments = Collections.emptyList();
	}

	public Selector(List<SelectorSegment> segments) {
		this.segments = new ArrayList<SelectorSegment>(segments);
	}

	public List<SelectorSegment> getSegments() {
		return segments;
	}
}
