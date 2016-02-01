package com.sortable.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	public Result[] matchUsingIndex() {
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
		
		Set<Listing> listingsMatched = new HashSet<Listing>();

		for (Product product : products) {
			Result result = new Result(product.getName());

			for (Listing listing : possibleMatchingsWithModel(product,
					indexOfTermsToListings)) {
				if (!listingsMatched.contains(listing) && manufacturerMatches(product, listing)) {
					listingsMatched.add(listing);
					result.add(listing);
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

	private Product searchTopOneProduct(Listing listing, Product[] products) {
		MatchingRelevance bestMatchingRelevance = MatchingRelevance.NO_RELEVANCE;
		Product topOneProduct = null;

		for (Product product : products) {

			String listingManufacturer = listing.getManufacturer()
					.toLowerCase();
			String productManufacturer = product.getManufacturer()
					.toLowerCase();

			if (listingManufacturer.contains(productManufacturer)
					|| productManufacturer.contains(listingManufacturer)) {

				if (listing.getTitle().toLowerCase()
						.contains(product.getName().toLowerCase())) {
					return product;
				}

				MatchingRelevance mr = macthingRelevance(listing, product);

				if (mr.isBetterThan(bestMatchingRelevance)) {
					bestMatchingRelevance = mr;
					topOneProduct = product;
				}
			}
		}

		if (bestMatchingRelevance.hasHighPrecision()) {
			return topOneProduct;
		}

		return null;
	}

	private MatchingRelevance macthingRelevance(Listing listing, Product product) {
		boolean containsModel = contains(listing.getTitle(), product.getModel());

		if (product.hasFamily()) {
			boolean containsFamily = contains(listing.getTitle(),
					product.getFamily());

			if (containsFamily && containsModel) {
				return MatchingRelevance.MODEL_AND_FAMILY;
			}

			if (containsFamily && !containsModel) {
				return MatchingRelevance.FAMILY_AND_NOT_MODEL;
			}

			if (!containsFamily && containsModel) {
				return MatchingRelevance.MODEL_AND_NOT_FAMILY;
			}
		} else if (containsModel) {
			return MatchingRelevance.MODEL;
		}

		return MatchingRelevance.NO_RELEVANCE;
	}

	private boolean contains(String source, String pattern) {
		source = source.toLowerCase();
		pattern = pattern.toLowerCase();

		if (containsHelper(source, pattern)) {
			return true;
		}

		String patternWithoutSpaces = pattern.replace(" ", "");

		if (containsHelper(source, patternWithoutSpaces)) {
			return true;
		}

		String patternWithoutDashes = pattern.replace("-", "");

		if (containsHelper(source, patternWithoutDashes)) {
			return true;
		}

		String patternWithoutUnderline = pattern.replace("_", "");

		if (containsHelper(source, patternWithoutUnderline)) {
			return true;
		}

		String patternWithoutSpacesAndDashes = pattern.replace(" ", "")
				.replace("-", "");

		if (containsHelper(source, patternWithoutSpacesAndDashes)) {
			return true;
		}

		String patternWithoutSpacesAndUnderlines = pattern.replace(" ", "")
				.replace("_", "");

		if (containsHelper(source, patternWithoutSpacesAndUnderlines)) {
			return true;
		}

		String patternWithoutDashesAndUnderlines = pattern.replace("-", "")
				.replace("_", "");

		if (containsHelper(source, patternWithoutDashesAndUnderlines)) {
			return true;
		}

		String patternWithoutSpacesDashesAndUnderlines = pattern
				.replace(" ", "").replace("-", "").replace("_", "");

		if (containsHelper(source, patternWithoutSpacesDashesAndUnderlines)) {
			return true;
		}

		String patternWithDashes = pattern.replace(" ", "-").replace("_", "-");

		if (containsHelper(source, patternWithDashes)) {
			return true;
		}

		String patternWithUnderline = pattern.replace(" ", "_").replace("-",
				"_");

		if (containsHelper(source, patternWithUnderline)) {
			return true;
		}

		String patternWithSpaces = pattern.replace("-", " ").replace("_", " ");

		if (containsHelper(source, patternWithSpaces)) {
			return true;
		}

		return false;
	}

	private boolean containsHelper(String source, String pattern) {
		if (source.equals(pattern)) {
			return true;
		}

		return source.startsWith(pattern + " ")
				|| source.endsWith(" " + pattern)
				|| source.contains(" " + pattern + " ");

	}
}
