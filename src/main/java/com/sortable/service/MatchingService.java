package com.sortable.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sortable.domain.Listing;
import com.sortable.domain.Matching;
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

		Set<Result> results = new HashSet<Result>();

		Map<Listing, Matching> listingsMatched = new HashMap<Listing, Matching>();
		Map<Product, Result> productsMatched = new HashMap<Product, Result>();

		for (Product product : products) {
			Result result = new Result(product.getName());
			productsMatched.put(product, result);

			tryMatchingWithModelAndFamily(indexOfTermsToListings, results,
					listingsMatched, productsMatched, product);

			tryMatchingWithModelWhenProductWithoutFamily(
					indexOfTermsToListings, results, listingsMatched,
					productsMatched, product);

			tryMatchingWithOnlyModelWhenProductWithFamily(
					indexOfTermsToListings, results, listingsMatched,
					productsMatched, product);

			if (!result.isEmpty()) {
				results.add(result);
			}
		}

		return results.toArray(new Result[] {});

	}

	private void tryMatchingWithModelAndFamily(
			Map<String, Set<Listing>> indexOfTermsToListings,
			Set<Result> results, Map<Listing, Matching> listingsMatched,
			Map<Product, Result> productsMatched, Product product) {

		Set<Listing> listings = possibleMatchingsWithModelAndFamily(product,
				indexOfTermsToListings);

		tryMatching(indexOfTermsToListings, results, listingsMatched,
				productsMatched, product, listings,
				MatchingRelevance.MODEL_AND_FAMILY);
	}

	private void tryMatchingWithModelWhenProductWithoutFamily(
			Map<String, Set<Listing>> indexOfTermsToListings,
			Set<Result> results, Map<Listing, Matching> listingsMatched,
			Map<Product, Result> productsMatched, Product product) {

		Set<Listing> listings = possibleMatchingsWithModelWhenProductWithoutFamily(
				product, indexOfTermsToListings);

		tryMatching(indexOfTermsToListings, results, listingsMatched,
				productsMatched, product, listings, MatchingRelevance.MODEL);
	}

	private void tryMatchingWithOnlyModelWhenProductWithFamily(
			Map<String, Set<Listing>> indexOfTermsToListings,
			Set<Result> results, Map<Listing, Matching> listingsMatched,
			Map<Product, Result> productsMatched, Product product) {

		Set<Listing> listings = possibleMatchingsWithOnlyModelWhenProductWithFamily(
				product, indexOfTermsToListings);

		tryMatching(indexOfTermsToListings, results, listingsMatched,
				productsMatched, product, listings,
				MatchingRelevance.MODEL_AND_NOT_FAMILY);
	}

	private void tryMatching(Map<String, Set<Listing>> indexOfTermsToListings,
			Set<Result> results, Map<Listing, Matching> listingsMatched,
			Map<Product, Result> productsMatched, Product product,
			Set<Listing> listings, MatchingRelevance relevance) {

		Result result = productsMatched.get(product);

		for (Listing listing : listings) {
			if (manufacturerMatched(product, listing)) {

				if (listingsMatched.containsKey(listing)) {

					Matching matching = listingsMatched.get(listing);

					Product productMatched = matching.getProduct();

					if (matching.getRelevance() == relevance) {
						maintainsGreaterModelLength(results, listingsMatched,
								productsMatched, product, listing,
								productMatched, relevance);
					} else if (relevance.isBetterThan(matching.getRelevance())) {
						changeProducts(results, listingsMatched,
								productsMatched, product, listing,
								productMatched, relevance);
					}

				} else {
					listingsMatched.put(listing, new Matching(product,
							relevance));
					result.add(listing);
				}

			}
		}
	}

	private void maintainsGreaterModelLength(Set<Result> results,
			Map<Listing, Matching> listingsMatched,
			Map<Product, Result> productsMatched, Product product,
			Listing listing, Product productMatched, MatchingRelevance relevance) {

		if (productMatched.getModelLower().length() < product.getModelLower()
				.length()) {
			changeProducts(results, listingsMatched, productsMatched, product,
					listing, productMatched, relevance);
		}
	}

	private void changeProducts(Set<Result> results,
			Map<Listing, Matching> listingsMatched,
			Map<Product, Result> productsMatched, Product product,
			Listing listing, Product productMatched, MatchingRelevance relevance) {
		listingsMatched.put(listing, new Matching(product, relevance));
		productsMatched.get(productMatched).remove(listing);

		if (productsMatched.get(productMatched).isEmpty()) {
			results.remove(productsMatched.get(productMatched));
		}

		Result result = productsMatched.get(product);

		result.add(listing);
	}

	private boolean manufacturerMatched(Product product, Listing listing) {
		return (listing.getManufacturerLower().contains(
				product.getManufacturerLower()) || product
				.getManufacturerLower()
				.contains(listing.getManufacturerLower()));
	}

	private Set<Listing> possibleMatchingsWithOnlyModelWhenProductWithFamily(
			Product product, Map<String, Set<Listing>> indexOfTermsToListings) {

		if (!product.hasFamily()) {
			return new HashSet<Listing>();
		}

		Set<Listing> possibleMatchingsWithModel = possibleMatchingsWithProductTerm(
				product.getModelLower(), indexOfTermsToListings);

		Set<Listing> possibleMatchingsWithFamily = possibleMatchingsWithProductTerm(
				product.getFamilyLower(), indexOfTermsToListings);

		possibleMatchingsWithModel.removeAll(possibleMatchingsWithFamily);

		return possibleMatchingsWithModel;
	}

	private Set<Listing> possibleMatchingsWithModelWhenProductWithoutFamily(
			Product product, Map<String, Set<Listing>> indexOfTermsToListings) {

		if (product.hasFamily()) {
			return new HashSet<Listing>();
		}

		return possibleMatchingsWithProductTerm(product.getModelLower(),
				indexOfTermsToListings);
	}

	private Set<Listing> possibleMatchingsWithModelAndFamily(Product product,
			Map<String, Set<Listing>> indexOfTermsToListings) {

		if (!product.hasFamily()) {
			return new HashSet<Listing>();
		}

		Set<Listing> possibleMatchingsWithModel = possibleMatchingsWithProductTerm(
				product.getModelLower(), indexOfTermsToListings);

		Set<Listing> possibleMatchingsWithFamily = possibleMatchingsWithProductTerm(
				product.getFamilyLower(), indexOfTermsToListings);

		possibleMatchingsWithModel.retainAll(possibleMatchingsWithFamily);

		return possibleMatchingsWithModel;

	}

	private Set<Listing> possibleMatchingsWithProductTerm(String term,
			Map<String, Set<Listing>> indexOfTermsToListings) {

		Set<Listing> result = new HashSet<Listing>();

		if (indexOfTermsToListings.containsKey(term)) {
			result.addAll(indexOfTermsToListings.get(term));
		}

		String termWithoutSpaces = term.replace(" ", "");

		if (indexOfTermsToListings.containsKey(termWithoutSpaces)) {
			result.addAll(indexOfTermsToListings.get(termWithoutSpaces));
		}

		if (term.indexOf(" ") != -1) {
			result.addAll(listingsWithAllModelTerms(term,
					indexOfTermsToListings));
		}

		return result;
	}

	private Set<Listing> listingsWithAllModelTerms(String term,
			Map<String, Set<Listing>> indexOfTermsToListings) {
		Set<Listing> result = new HashSet<Listing>();

		String[] terms = term.split(" ");

		for (int i = 0; i < terms.length; i++) {
			if (indexOfTermsToListings.containsKey(terms[i])) {

				if (i == 0) {
					result.addAll(indexOfTermsToListings.get(terms[i]));
				} else {
					result.retainAll(indexOfTermsToListings.get(terms[i]));
				}

			} else {
				return new HashSet<Listing>();
			}
		}

		return result;
	}
}