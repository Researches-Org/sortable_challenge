package com.sortable.domain;

import java.util.ArrayList;
import java.util.List;

public class Result {

	private String product_name;
	private List<Listing> listings;

	public Result(String productName) {
		this.product_name = productName;
		this.listings = new ArrayList<Listing>();
	}

	public String getProductName() {
		return product_name;
	}

	public void add(Listing listing) {
		listings.add(listing);
	}

}
