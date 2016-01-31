package com.sortable;

import com.sortable.domain.Result;
import com.sortable.service.FileService;
import com.sortable.service.ListingService;
import com.sortable.service.MatchingService;
import com.sortable.service.ProductService;

public class Main {

	public static void main(String[] args) {
		FileService fileService = new FileService();
		ProductService productService = new ProductService(fileService);
		ListingService listingService = new ListingService(fileService);

		MatchingService matchingService = new MatchingService(productService,
				listingService);
		Result[] results = matchingService.match();

		fileService.writeFile(results);
	}
}
