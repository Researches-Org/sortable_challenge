package com.sortable.domain;

public class Matching {
	
	private Product product;
	
	private MatchingRelevance relevance;

	public Matching(Product product, MatchingRelevance relevance) {
		this.product = product;
		this.relevance = relevance;
	}

	public Product getProduct() {
		return product;
	}

	public MatchingRelevance getRelevance() {
		return relevance;
	}

}
