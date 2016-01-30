package com.sortable.service;

import java.util.HashMap;
import java.util.Map;

import com.sortable.domain.Listing;
import com.sortable.domain.MatchingRelevance;
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

		Map<Product, Result> results = new HashMap<Product, Result>();

		for (Listing listing : listings) {
			Product product = searchTopOneProduct(listing, products);

			if (product != null) {
				if (!results.containsKey(product)) {
					results.put(product, new Result(product.getName()));
				}
				results.get(product).add(listing);
			}
		}

		return results.values().toArray(new Result[] {});
	}

	private Product searchTopOneProduct(Listing listing, Product[] products) {
		MatchingRelevance bestMatchingRelevance = MatchingRelevance.NO_RELEVANCE;
		Product topOneProduct = null;

		for (Product product : products) {
			if (contains(listing.getManufacturer(), product.getManufacturer())) {

				if (listing.getTitle().contains(product.getName())) {
					return product;
				}

				MatchingRelevance mr = macthingRelevance(listing, product);

				if (mr.isBetterThan(bestMatchingRelevance)) {
					bestMatchingRelevance = mr;
					topOneProduct = product;

					if (bestMatchingRelevance.hasHighPrecision()) {
						return topOneProduct;
					}

				}
			}
		}

		return null;
	}

	private MatchingRelevance macthingRelevance(Listing listing, Product product) {
		if (product.hasFamily()) {
			if (contains(listing.getTitle(), product.getFamily())
					&& contains(listing.getTitle(), product.getModel())) {
				return MatchingRelevance.MODEL_AND_FAMILY;
			}

			if (contains(listing.getTitle(), product.getFamily())
					&& !contains(listing.getTitle(), product.getModel())) {
				return MatchingRelevance.FAMILY_AND_NOT_MODEL;
			}

			if (!contains(listing.getTitle(), product.getFamily())
					&& contains(listing.getTitle(), product.getModel())) {
				return MatchingRelevance.MODEL_AND_NOT_FAMILY;
			}
		} else if (contains(listing.getTitle(), product.getModel())) {
			return MatchingRelevance.MODEL;
		}

		return MatchingRelevance.NO_RELEVANCE;
	}

	private boolean contains(String source, String pattern) {
		if (source.equals(pattern)) {
			return true;
		}

		if (source.startsWith(pattern + " ") || source.endsWith(" " + pattern)
				|| source.contains(" " + pattern + " ")) {
			return true;
		}

		String patternWithoutSpacesAndDashes = pattern.replace(" ", "")
				.replace("-", "");

		if (source.startsWith(patternWithoutSpacesAndDashes + " ")
				|| source.endsWith(" " + patternWithoutSpacesAndDashes)
				|| source.contains(" " + patternWithoutSpacesAndDashes + " ")) {
			return true;
		}

		return false;
	}
}
