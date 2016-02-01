package com.sortable.domain;

public enum MatchingRelevance {

	MODEL_AND_FAMILY(0),

	MODEL(1),

	MODEL_AND_NOT_FAMILY(3);

	private int relevance;

	private MatchingRelevance(int relevance) {
		this.relevance = relevance;
	}

	public boolean isBetterThan(MatchingRelevance matchingRelevance) {
		return this.relevance < matchingRelevance.relevance;
	}

}