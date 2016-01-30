package com.sortable.service;

import java.util.List;

import com.sortable.domain.Listing;

public class ListingService {

	private FileService fileService;

	public ListingService(FileService fileService) {
		this.fileService = fileService;
	}

	public Listing[] getListings() {
		List<Listing> listings = fileService.readFile("./listings.txt",
				Listing.class);
		return listings.toArray(new Listing[] {});
	}

}
