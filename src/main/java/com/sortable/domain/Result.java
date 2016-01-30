package com.sortable.domain;

import java.util.ArrayList;
import java.util.List;

public class Result {
	
	private String productName;
	private List<Listing> listings;
	
	public Result(String productName) {
		this.productName = productName;
		this.listings = new ArrayList<Listing>();
	}
	
	public void add(Listing listing) {
		listings.add(listing);
	}

}
