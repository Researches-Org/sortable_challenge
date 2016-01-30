package com.sortable.domain;

public enum MatchingRelevance {

	MODEL(2),

	MODEL_AND_FAMILY(2),

	MODEL_AND_NOT_FAMILY(2),

	FAMILY_AND_NOT_MODEL(3),

	NO_RELEVANCE(4);

	private int relevance;

	private MatchingRelevance(int relevance) {
		this.relevance = relevance;
	}

	public boolean hasHighPrecision() {
		if (this == MODEL_AND_FAMILY || this == MODEL
				|| this == MODEL_AND_NOT_FAMILY) {
			return true;
		}

		return false;
	}

	public boolean isBetterThan(MatchingRelevance matchingRelevance) {
		return this.relevance < matchingRelevance.relevance;
	}

}
