package com.sortable.domain;

import java.util.HashSet;
import java.util.Set;

public class Result {

	private String product_name;
	private Set<Listing> listings;

	public Result(String productName) {
		this.product_name = productName;
		this.listings = new HashSet<Listing>();
	}

	public String getProductName() {
		return product_name;
	}

	public void add(Listing listing) {
		listings.add(listing);
	}

	public boolean isEmpty() {
		return listings.isEmpty();
	}

	public void remove(Listing listing) {
		listings.remove(listing);
	}

}
