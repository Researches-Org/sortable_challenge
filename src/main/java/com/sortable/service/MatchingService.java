package com.sortable.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sortable.domain.Listing;
import com.sortable.domain.Product;
import com.sortable.domain.Result;

public class MatchingService {

	private ProductService productService;

	private ListingService listingService;

	public MatchingService(ProductService productService,
			ListingService listingService) {
		this.productService = productService;
		this.listingService = listingService;
	}

	public Result[] match() {
		Product[] products = productService.getProducts();
		Listing[] listings = listingService.getListings();

		Map<String, Set<Listing>> indexOfTermsToListings = createIndexOfTermsToListings(listings);

		return matchProducts(products, indexOfTermsToListings);

	}

	private Map<String, Set<Listing>> createIndexOfTermsToListings(
			Listing[] listings) {
		Map<String, Set<Listing>> indexOfTerms = new HashMap<String, Set<Listing>>();

		for (Listing listing : listings) {
			String[] terms = listing.getTitleLower().split(" ");

			for (String term : terms) {
				if (!indexOfTerms.containsKey(term)) {
					indexOfTerms.put(term, new HashSet<Listing>());
				}
				indexOfTerms.get(term).add(listing);
			}
		}

		return indexOfTerms;
	}

	private Result[] matchProducts(Product[] products,
			Map<String, Set<Listing>> indexOfTermsToListings) {

		List<Result> results = new ArrayList<Result>();

		Map<Listing, Product> listingsMatched = new HashMap<Listing, Product>();
		Map<Product, Result> productsMatched = new HashMap<Product, Result>();

		for (Product product : products) {
			Result result = new Result(product.getName());
			productsMatched.put(product, result);

			for (Listing listing : possibleMatchingsWithModel(product,
					indexOfTermsToListings)) {
				if (manufacturerMatches(product, listing)) {

					if (listingsMatched.containsKey(listing)) {

						Product productMatched = listingsMatched.get(listing);

						if (productMatched.getModelLower().length() < product
								.getModelLower().length()) {
							listingsMatched.put(listing, product);
							productsMatched.get(productMatched).remove(listing);

							if (productsMatched.get(productMatched).isEmpty()) {
								results.remove(productsMatched
										.get(productMatched));
							}

							result.add(listing);
						}

					} else {
						listingsMatched.put(listing, product);
						result.add(listing);
					}

				}
			}

			if (!result.isEmpty()) {
				results.add(result);
			}
		}

		return results.toArray(new Result[] {});

	}

	private boolean manufacturerMatches(Product product, Listing listing) {
		return (listing.getManufacturerLower().contains(
				product.getManufacturerLower()) || product
				.getManufacturerLower()
				.contains(listing.getManufacturerLower()));
	}

	private Set<Listing> possibleMatchingsWithModel(Product product,
			Map<String, Set<Listing>> indexOfTermsToListings) {

		String model = product.getModelLower();

		Set<Listing> result = new HashSet<Listing>();

		if (indexOfTermsToListings.containsKey(model)) {
			result.addAll(indexOfTermsToListings.get(model));
		}

		String modelWithoutSpaces = model.replace(" ", "");

		if (indexOfTermsToListings.containsKey(modelWithoutSpaces)) {
			result.addAll(indexOfTermsToListings.get(modelWithoutSpaces));
		}

		if (model.indexOf(" ") != -1) {
			result.addAll(listingsWithAllModelTerms(model,
					indexOfTermsToListings));
		}

		return result;
	}

	private Set<Listing> listingsWithAllModelTerms(String model,
			Map<String, Set<Listing>> indexOfTermsToListings) {
		Set<Listing> result = new HashSet<Listing>();

		String[] modelTerms = model.split(" ");

		for (int i = 0; i < modelTerms.length; i++) {
			if (indexOfTermsToListings.containsKey(modelTerms[i])) {

				if (i == 0) {
					result.addAll(indexOfTermsToListings.get(modelTerms[i]));
				} else {
					result.retainAll(indexOfTermsToListings.get(modelTerms[i]));
				}

			} else {
				return new HashSet<Listing>();
			}
		}

		return result;
	}

}
