package com.sortable;

import com.sortable.domain.Result;
import com.sortable.service.ListingService;
import com.sortable.service.MatchingService;
import com.sortable.service.ProductService;

public class Main {

	public static void main(String[] args) {
		ProductService productService = new ProductService();
		ListingService listingService = new ListingService();

		MatchingService matchingService = new MatchingService(productService,
				listingService);

		Result[] results = matchingService.match();
	}

}
