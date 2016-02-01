package com.sortable.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Result {

	private String product_name;
	private List<Listing> listings;
	private Set<Listing> listingsSet;

	public Result(String productName) {
		this.product_name = productName;
		this.listings = new ArrayList<Listing>();
		this.listingsSet = new HashSet<Listing>();
	}

	public String getProductName() {
		return product_name;
	}

	public void add(Listing listing) {
		listings.add(listing);
		listingsSet.addAll(listings);
	}
	
	public boolean isEmpty() {
		return listingsSet.isEmpty();
	}

}
