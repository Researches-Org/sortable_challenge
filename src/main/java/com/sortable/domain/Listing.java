package com.sortable.domain;

public class Listing {

	private String title;
	private String manufacturer;
	private String currency;
	private String price;
	
	public Listing(String title, String manufacturer, String currency,
			String price) {
		this.title = title;
		this.manufacturer = manufacturer;
		this.currency = currency;
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getCurrency() {
		return currency;
	}

	public String getPrice() {
		return price;
	}
	
	public String getTitleLower() {
		return title.toLowerCase().replaceAll("[^a-z0-9]", " ");
	}
	
	public String getManufacturerLower() {
		return manufacturer.toLowerCase();
	}
}
